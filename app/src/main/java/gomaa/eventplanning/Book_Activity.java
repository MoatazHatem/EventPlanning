package gomaa.eventplanning;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Book_Activity extends AppCompatActivity {

    private TextView tvtitle, tvdescription, tvSalary;
    private ImageView img;

    private Button addToCartButton;
    private DatabaseReference mDatabaseReference;
    private Toolbar mtToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_);

        mtToolbar= (Toolbar) findViewById(R.id.cart_appBar);
        setSupportActionBar(mtToolbar);
        getSupportActionBar().setTitle("Add Card");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mtToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvtitle = findViewById(R.id.txttitle);
        tvdescription = findViewById(R.id.txtDesc);
        tvSalary = findViewById(R.id.txtCat);
        img = findViewById(R.id.bookthumbnail);

        addToCartButton= findViewById(R.id.addToCart_button);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Cart");

        // Recieve data
        Intent intent = getIntent();
        final String Title = intent.getExtras().getString("Title");
        String Description = intent.getExtras().getString("Description");
        final String image = intent.getExtras().getString("Thumbnail");
        final String Salary = intent.getExtras().getString("Salary");

        // Setting values
        tvtitle.setText(Title);
        tvdescription.setText(Description);
        tvSalary.setText(Salary);
        Picasso.get().load(image).into(img);


        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ItemModel itemModel = new ItemModel();
                itemModel.setItemName(Title);
                itemModel.setItemSalary(Salary);
                itemModel.setImgUrl(image);


                String uploadID = mDatabaseReference.push().getKey();
                itemModel.setmKey(uploadID);
                mDatabaseReference.child(uploadID).setValue(itemModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Book_Activity.this, "تم الرفع بنجاح", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Book_Activity.this,MainActivity.class));

                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Book_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

                /*Intent intent = new Intent(Book_Activity.this,ShoppingCartActivity.class);

                // passing data to the book activity
                intent.putExtra("Title", tvtitle.getText().toString());

                intent.putExtra("Salary",tvSalary.getText().toString());
                intent.putExtra("Thumbnail",image);
                // start the activity
                startActivity(intent);*/
            }
        });
    }
}
