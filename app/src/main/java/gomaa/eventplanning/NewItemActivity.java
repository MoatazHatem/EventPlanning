package gomaa.eventplanning;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewItemActivity extends AppCompatActivity {

    private EditText mName, mSalary;
    TextInputLayout mDescription;
    private Button mAddbtn;
    private String mCurrentUserID;
    private CircleImageView userProfileImage;


    private static final int GallaryPick = 1;

    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference mDatabaseReference;
    private StorageTask mUploadTask;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private StorageReference mStorageRef;

    private ProgressDialog loadingBar;

    private Uri imgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserID = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference("uploads");
        // mStorageRef = FirebaseStorage.getInstance().getReference().child("Item Images");
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        mName = findViewById(R.id.name);
        mSalary = findViewById(R.id.salary);
        mDescription = findViewById(R.id.description);
        mAddbtn = findViewById(R.id.addButton);
        userProfileImage = findViewById(R.id.logo);

        loadingBar = new ProgressDialog(this);


        mAddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(NewItemActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImage();
                }

            }
        });
        // RetrieveUserInfo();

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallaryIntent = new Intent();
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                gallaryIntent.setType("image/*");
                startActivityForResult(gallaryIntent, GallaryPick);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GallaryPick && resultCode == RESULT_OK && data != null) {
            imgUri = data.getData();

            Picasso.get().load(imgUri).resize(100, 100).centerCrop().into(userProfileImage);


        }


    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {


        /*ItemModel itemModel = new ItemModel(mName.getText().toString(), mSalary.getText().toString());

        String uploadID = mDataBaseRef.push().getKey();


        mDataBaseRef.child(uploadID).setValue(itemModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(NewItemActivity.this, "Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewItemActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/

        if (imgUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imgUri));

            mUploadTask = fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingBar.setProgress(0);

                        }
                    }, 500);

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(NewItemActivity.this, "success", Toast.LENGTH_SHORT).show();
                            ItemModel itemModel = new ItemModel(mName.getText().toString(), uri.toString(), mSalary.getText().toString(), mDescription.getEditText().getText().toString());

                            String uploadID = mDatabaseReference.push().getKey();
                            itemModel.setmKey(uploadID);
                            mDatabaseReference.child(uploadID).setValue(itemModel);
                            sendUserToMainActivity();

                        }
                    });

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            loadingBar.setProgress((int) progress);


                        }
                    });


        } else {
            Toast.makeText(this, "no file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendUserToMainActivity() {

        Intent mainIntent = new Intent(NewItemActivity.this, MainActivity.class);

        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//User cant back to Register activity
        startActivity(mainIntent);
        finish();

    }

}
