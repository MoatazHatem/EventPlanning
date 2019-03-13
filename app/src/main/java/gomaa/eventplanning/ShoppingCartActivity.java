package gomaa.eventplanning;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity implements OnItemClickListener {

    private List<ItemModel> mItems;

    private RecyclerView myrv;


    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabaseReference;
    private FirebaseStorage mStorage;
    private ShoppingCartAdapter cartAdapter;
    private Toolbar mtToolbar;



    //مدينة نصر الحي السابع ش زكريا عثمان 11 الدور التاني
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

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


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //mDatabaseReference = FirebaseDatabase.getInstance().getReference("Cart");

        mStorage = FirebaseStorage.getInstance();

        myrv = findViewById(R.id.recyclerview_id);
        myrv.setHasFixedSize(true);
        myrv.setLayoutManager(new GridLayoutManager(this,1));


        /*Intent intent = getIntent();
        String Title = intent.getExtras().getString("Title");
        String image = intent.getExtras().getString("Thumbnail");
        String Salary = intent.getExtras().getString("Salary");*/

        mItems = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Cart");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mItems.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ItemModel itemModel = postSnapshot.getValue(ItemModel.class);
                    mItems.add(itemModel);
                }
                cartAdapter = new ShoppingCartAdapter(ShoppingCartActivity.this,mItems);
                cartAdapter.notifyDataSetChanged();
                myrv.setAdapter(cartAdapter);
                cartAdapter.setOnItemClickListener(ShoppingCartActivity.this);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShoppingCartActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });

       /* shoppingSalaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/



         /*       mItems = new ArrayList<>();
        ItemModel itemModel = new ItemModel();
        itemModel.setItemName(Title);
        itemModel.setItemSalary(Salary);
        itemModel.setImgUrl(image);

        mItems.add(itemModel);
*/


        /*String uploadID = mDatabaseReference.push().getKey();
        mDatabaseReference.child(uploadID).setValue(itemModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(ShoppingCartActivity.this, "تم الرفع بنجاح", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShoppingCartActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });*/






    }

    @Override
    public void onDeleteClick(int position) {
        ItemModel selectedItem = mItems.get(position);
        final String selectedKey = selectedItem.getmKey();

        mDatabaseReference.child(selectedKey).removeValue();
        Toast.makeText(ShoppingCartActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onWhatEverClick(int position) {

    }
}
