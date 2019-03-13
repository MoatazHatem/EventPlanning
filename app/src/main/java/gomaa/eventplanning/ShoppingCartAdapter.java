package gomaa.eventplanning;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder> {


    private Context mContext;
    private List<ItemModel> mData;
    Button shoppingSalaryButton;

    private OnItemClickListener clickInterface ;


    public ShoppingCartAdapter(Context mContext, List<ItemModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_shopping_cart, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i)
    {

        myViewHolder.shoppingTitle_TextView.setText(mData.get(i).getItemName());
        myViewHolder.shoppingSalary_TextView.setText(mData.get(i).getItemSalary());
        Picasso.get().load(mData.get(i).getImgUrl()).into(myViewHolder.shoppingImg_TextView);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shoppingTitle_TextView;
        TextView shoppingSalary_TextView;
        ImageView shoppingImg_TextView;
        CardView shoppingCardView;
        Button shoppingSalaryButton;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);




            shoppingTitle_TextView = (TextView) itemView.findViewById(R.id.shoppingTitle);
            shoppingSalary_TextView = (TextView) itemView.findViewById(R.id.shoppingSalary);
            shoppingImg_TextView = (ImageView) itemView.findViewById(R.id.shoppingImg);
            shoppingCardView= (CardView) itemView.findViewById(R.id.shoppingCardview_id);
            shoppingSalaryButton = itemView.findViewById(R.id.shoppingSalaryButton);
            shoppingSalaryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickInterface.onDeleteClick(getAdapterPosition());
                }
            });


        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        clickInterface = listener;
    }
}
