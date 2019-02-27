package je.digital.kevin_pickmeup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import je.digital.kevin_pickmeup.constants.Status;
import je.digital.kevin_pickmeup.models.Pickup;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHoder>{

    final FirebaseDatabase fb = FirebaseDatabase.getInstance();
    DatabaseReference mRef = fb.getReference();

    List<Pickup> list;
    Context context;

    public RecyclerAdapter(List<Pickup> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card,parent,false);

        return new MyHoder(view);
    }

    @Override
    public void onBindViewHolder(MyHoder holder, int position) {
        Pickup mylist = list.get(position);
        holder.location.setText(mylist.getLocation());
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {

        int arr = 0;

        try{
            if(list.size()==0){

                arr = 0;

            }
            else{

                arr=list.size();
            }



        }catch (Exception e){



        }

        return arr;

    }

    public class MyHoder extends RecyclerView.ViewHolder{
        TextView location;
        Button btnAccept, btnDeny;

        public MyHoder(View itemView) {
            super(itemView);

            location = (TextView) itemView.findViewById(R.id.name);
            btnAccept = (Button) itemView.findViewById(R.id.btnAccept);
            btnDeny = (Button) itemView.findViewById(R.id.btnDeny);

        }

        public void bind(final Pickup pickup) {
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = pickup.getLocation();
                    String[] lArr = str.substring(10, str.length()-1).split(",");

                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+Double.valueOf(lArr[0]+Double.valueOf(lArr[1])));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
                }
            });

            btnDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRef.child("Pickups").child(pickup.getId()).child("status").setValue(Status.NOTACTIVE);
                }
            });
        }
    }

}