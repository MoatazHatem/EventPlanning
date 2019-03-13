package gomaa.eventplanning;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener , NavigationView.OnNavigationItemSelectedListener{

   // private Button btnCart;
    private FloatingActionButton fab_add_reading;
    private List<ItemModel> mItems;


    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabaseReference;
    private RecyclerViewAdapter myAdapter;

    private RecyclerView myrv;

    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle.syncState();



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //btnCart=findViewById(R.id.btnCart);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        myrv.setHasFixedSize(true);
        myrv.setLayoutManager(new GridLayoutManager(this,2));


    /*    btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent ShoppingIntent = new Intent(MainActivity.this, ShoppingCartActivity.class);
                    startActivity(ShoppingIntent);

            }
        });*/

      //  final RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        /*final RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, mItems);
        myrv.setLayoutManager(new GridLayoutManager(this, 3));
        myrv.setAdapter(myAdapter);*/

        fab_add_reading = findViewById(R.id.fab_add_reading);

        fab_add_reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newItemIntent = new Intent(MainActivity.this, NewItemActivity.class);
                startActivity(newItemIntent);
            }
        });


        mItems = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mItems.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ItemModel itemModel = postSnapshot.getValue(ItemModel.class);

                    mItems.add(itemModel);
                }


                myAdapter = new RecyclerViewAdapter(MainActivity.this, mItems);
                myAdapter.notifyDataSetChanged();

                myrv.setAdapter(myAdapter);

                myAdapter.setOnItemClickListener(MainActivity.this);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null) {
            sendUserToLoginActivity();
        } else {
            VerifyUserExistance();
        }
    }

    private void VerifyUserExistance() {

        String currentUserID = mAuth.getCurrentUser().getUid();


        mDatabaseReference.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if ((dataSnapshot.child("name").exists())) {


                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(MainActivity.this, "else", Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//User cant back to Register activity

        startActivity(loginIntent);
        finish();
    }

    @Override
    public void onDeleteClick(int position) {
        ItemModel selectedItem = mItems.get(position);
        final String selectedKey = selectedItem.getmKey();

        mDatabaseReference.child(selectedKey).removeValue();
        Toast.makeText(MainActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_cart:

                Intent ShoppingIntent = new Intent(MainActivity.this, ShoppingCartActivity.class);
                startActivity(ShoppingIntent);

                break;
            case R.id.nav_last:
                Toast.makeText(this, "last order", Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_contact:
                Toast.makeText(this, "contact us", Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


