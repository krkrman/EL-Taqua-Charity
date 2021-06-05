package com.example.el_taquacharity.ui.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.el_taquacharity.R;
import com.example.el_taquacharity.databinding.ActivityScanQrCodeBinding;
import com.example.el_taquacharity.pojo.Family;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

import java.util.List;

public class ScanQrCodeActivity extends AppCompatActivity {

    private static final String TAG = "Data failure";
    boolean isDetected = false;
    FirebaseVisionBarcodeDetector detector;
    FirebaseVisionBarcodeDetectorOptions options;
    ActivityScanQrCodeBinding binding;

    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;

    TextView idTextView, husbandNameTextView, wifeNameTextView, statusTextView
            , phoneNumberTextView,socialStatusTextView ,sonsNumberTextView
            , workTextView ,addressTextView , incomeTextView ,deptTextView;
    MaterialRippleLayout updateButton ;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference familyRef = db.collection("Families");

    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanQrCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        receiveData();
        initBottomSheet();
        Dexter.withContext(this).withPermissions(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        setUpCamera();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();


    }

    void initBottomSheet(){
        bottomSheetDialog = new BottomSheetDialog(ScanQrCodeActivity.this,R.style.BottomSheetDialogTheme);
        bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.family_bottom_sheet,
                (LinearLayout)findViewById(R.id.bottom_sheet_container));//here write the container id

        idTextView = bottomSheetView.findViewById(R.id.id_text_view);
        husbandNameTextView = bottomSheetView.findViewById(R.id.husband_name_text_view);
        wifeNameTextView = bottomSheetView.findViewById(R.id.wife_name_text_view);
        statusTextView = bottomSheetView.findViewById(R.id.status_text_view);
        phoneNumberTextView = bottomSheetView.findViewById(R.id.phone_number_text_view);
        socialStatusTextView = bottomSheetView.findViewById(R.id.social_status_text_view);
        sonsNumberTextView = bottomSheetView.findViewById(R.id.sons_number_text_view);
        workTextView = bottomSheetView.findViewById(R.id.work_text_view);
        addressTextView = bottomSheetView.findViewById(R.id.adress_text_view);
        incomeTextView = bottomSheetView.findViewById(R.id.income_text_view);
        deptTextView = bottomSheetView.findViewById(R.id.dept_text_view);
        updateButton = bottomSheetView.findViewById(R.id.update_family_button);
    }

    void setBottomSheetData(final Family family){
        idTextView.setText(this.getResources().getString(R.string.id_number)+" : " +family.getID());
        husbandNameTextView.setText(this.getResources().getString(R.string.husband_name)+" : " +family.getHusbandName());
        wifeNameTextView.setText(this.getResources().getString(R.string.wife_name)+" : " +family.getWifeName());
        statusTextView.setText(this.getResources().getString(R.string.status)+" : " +family.getStatus());
        phoneNumberTextView.setText(this.getResources().getString(R.string.phone_number)+" : " +family.getPhoneNumber());
        socialStatusTextView.setText(this.getResources().getString(R.string.social_status)+" : " +family.getSocialStatus());
        sonsNumberTextView.setText(this.getResources().getString(R.string.sons_number)+" : " +family.getSonsNumber());
        workTextView.setText(this.getResources().getString(R.string.work)+" : " +family.getWork());
        addressTextView.setText(this.getResources().getString(R.string.address)+" : " +family.getAddress());
        incomeTextView.setText(this.getResources().getString(R.string.income)+" : " + family.getIncome());
        deptTextView.setText(this.getResources().getString(R.string.dept)+" : " + family.getDept());
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanQrCodeActivity.this, AddPeopleActivity.class);
                intent.putExtra("Family" , family);
                startActivity(intent);
            }
        });
        startBottomSheet();
    }

    void startBottomSheet(){
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void setUpCamera() {
        binding.cameraView.setLifecycleOwner(this);
        binding.cameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                processImage(getVisionImageFromFrame(frame));
            }
        });

        options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE).build();
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
    }

    private void processImage(FirebaseVisionImage image) {
        if (!isDetected){
            detector.detectInImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                            processResult(firebaseVisionBarcodes);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ScanQrCodeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
        if (firebaseVisionBarcodes.size() > 0){
            isDetected = true;
            for (FirebaseVisionBarcode item : firebaseVisionBarcodes){
                int valueType = item.getValueType();
                switch (valueType){
                    case FirebaseVisionBarcode.TYPE_TEXT:{
                        getFamilyData(item.getRawValue());
                        break;
                    }
                    case FirebaseVisionBarcode.TYPE_URL:{
                        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(item.getRawValue()));
                        startActivity(intent);
                        break;
                    }
                    case FirebaseVisionBarcode.TYPE_CONTACT_INFO:{
                        String info = new StringBuilder("Name : ")
                                .append(item.getContactInfo().getName().getFormattedName())
                                .append("\n")
                                .append("Address : ")
                                .append(item.getContactInfo().getAddresses().get(0).getAddressLines())
                                .append("\n")
                                .append("Email: ")
                                .append(item.getContactInfo().getEmails().get(0).getAddress())
                                .toString();

                        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

    private void getFamilyData(String familyId) {
        familyRef.document(familyId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Family family = documentSnapshot.toObject(Family.class);
                    family.setID(familyId);
                    if (from.equals("EventDataActivity")) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("scannedFamily", family);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                    setBottomSheetData(family);
                } else {
                    Toast.makeText(ScanQrCodeActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ScanQrCodeActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });

    }

    private FirebaseVisionImage getVisionImageFromFrame(Frame frame) {
        byte[] data = frame.getData();
        FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setHeight(frame.getSize().getHeight())
                .setWidth(frame.getSize().getWidth())
                //.setRotation(frame.getRotation())  // only use it if you wanna work in landscape mode
                .build();
        return FirebaseVisionImage.fromByteArray(data,metadata);
    }

    void receiveData(){
        if (getIntent().getStringExtra("From") != null)
            from = getIntent().getStringExtra("From");
    }
}