package com.example.mainproject2.StockPlatform;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.example.mainproject2.ExpenseManager.FirebaseAdapter;
import com.example.mainproject2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.MyViewHolder> {
    Context context;

    ArrayList<userStocks> list;
    ArrayList<String> json;

    public PortfolioAdapter(Context context, ArrayList<userStocks> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.portfolio_info,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        userStocks u = list.get(position);
        holder.symbol.setText(u.getSymbol());
        holder.qty.setText(u.getQty());
        holder.date.setText(u.getDate());
        holder.buyPrice.setText("$"+u.getStName());



        OkHttpClient client = new OkHttpClient();

        String url ="https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="+u.getSymbol()+"&apikey=OOEPKQDKWRUGS4HV";

        Request request = new Request.Builder().url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String myRes = response.body().string();

                    ((MyPortfolioActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                             System.out.println(myRes);
                            try {
                                String close = new JSONObject(myRes).getJSONObject("Global Quote").getString("05. price");

                                DecimalFormat df = new DecimalFormat("#.##");
                                String price =  df.format(Double.parseDouble(close));

                                double buy = Double.parseDouble(u.getStName());
                                double current = Double.parseDouble(price);

                                double increase = current - buy;

                                double per = (increase/buy)*100;

                                System.out.println(per);

                                u.setPrice(price);
                                holder.prevClose.setText(price);

                                if (per > 0){
                                    holder.pchange.setTextColor(Color.parseColor("#00873C"));
                                    holder.pchange.setText("+"+df.format(per)+"%");
                                }else{
                                    holder.pchange.setTextColor(Color.RED);
                                    holder.pchange.setText("-"+df.format(per)+"%");
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        holder.prevClose.setText(u.getPrice());

        String key = u.getKey();
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
              AlertDialog dialog = new AlertDialog.Builder(context)
                      .setCancelable(false)
                      .setTitle("Are you sure? The transaction will be deleted.")
                      .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              dialogInterface.dismiss();
                          }
                      }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              list.remove(holder.getAdapterPosition());
                              MyPortfolioFirebaseAdapter fb = new MyPortfolioFirebaseAdapter();
                              System.out.println(key);
                              fb.remove(u.getKey()).addOnCompleteListener(task -> {
                                  System.out.println("hello3");
                                  if (task.isSuccessful()) {
                                      Toast.makeText(context, "Data deleted", Toast.LENGTH_SHORT).show();
                                      notifyItemRemoved(holder.getAdapterPosition());
                                  } else {
                                      Toast.makeText(context, "Data not deleted", Toast.LENGTH_SHORT).show();
                                  }
                              });
                              dialog.dismiss();
                              notifyDataSetChanged();
                              notifyItemRangeChanged(holder.getAdapterPosition(), getItemCount(), null);
                          }
                      }).create();

              dialog.show();



              return false;
          }
      });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView symbol,qty,buyPrice,date,prevClose,pchange;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            symbol = itemView.findViewById(R.id.symbol);
            qty = itemView.findViewById(R.id.qty);
            buyPrice = itemView.findViewById(R.id.buyPrice);
            date = itemView.findViewById(R.id.date);
            prevClose = itemView.findViewById(R.id.PrevClose);
            pchange = itemView.findViewById(R.id.pchange);



        }


    }
}


