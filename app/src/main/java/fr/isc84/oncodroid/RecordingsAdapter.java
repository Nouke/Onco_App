package fr.isc84.oncodroid;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.viewHolder> {

    Context context;
    ArrayList<ModelRecordings> audioArrayList;
    public OnItemClickListener onItemClickListener;

    public RecordingsAdapter(Context context, ArrayList<ModelRecordings> audioArrayList) {
        this.context = context;
        this.audioArrayList = audioArrayList;
    }

    @Override
    public RecordingsAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recordings_list, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecordingsAdapter.viewHolder holder, final int i) {
        holder.title.setText(audioArrayList.get(i).getTitle());
        holder.date.setText(audioArrayList.get(i).getDate());
        holder.duration.setText(audioArrayList.get(i).getDuration());
    }

    @Override
    public int getItemCount() {
        return audioArrayList.size();
    }

    public void getLayoutParams(){

    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, date, duration;
        ImageView send;
        public viewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            duration = (TextView) itemView.findViewById(R.id.duration);
            send = (ImageView) itemView.findViewById(R.id.send);

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                //    onItemClickListener.onItemClick(getAdapterPosition(), v);
                    System.out.println(audioArrayList.get(getAdapterPosition()).getTitle());

                    new Thread(new Runnable() {

                        public void run() {
                            FTPClient con = null;

                            try
                            {
                                con = new FTPClient();
                                // if you want to run this programm you have to add some corrects
                                // informations about your ftp server such as ip adress, username and password

                                // con.connect("51.0.0.0",21);

                               // if (con.login("test", "test"))
                                {
                                    con.enterLocalPassiveMode(); // important!
                                    con.setFileType(FTP.BINARY_FILE_TYPE);
                                    String data = audioArrayList.get(getAdapterPosition()).getUri().toString();

                                    FileInputStream in = new FileInputStream(new File(data));
                                   boolean result = con.storeFile("/app_mobile_test/"+audioArrayList.get(getAdapterPosition()).getTitle(), in);
                                   // boolean result = con.storeFile("/public_ftp/incoming/"+audioArrayList.get(getAdapterPosition()).getTitle(), in);
                                    in.close();
                                    if (result) {
                                       // Log.v("upload result", "succeeded");
                                        Snackbar snackbar = Snackbar.make(itemView.getRootView(), "Dictée envoyée avec succes", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null);
                                        View sbView = snackbar.getView();
                                        // sbView.setBackgroundColor(Color.rgb(0, 128, 128));
                                        sbView.setBackgroundColor(Color.rgb(46, 204, 113));
                                        snackbar.show();

                                    }
                                    con.logout();
                                    con.disconnect();
                                }
                            }
                            catch (Exception e)
                            {
                                Snackbar snackbar = Snackbar.make(itemView.getRootView(), "Echec d'envoi de la dictée, veuillez réesayer plus tard ou contacter l'equipe informatique", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(Color.RED);
                                snackbar.show();
                                e.printStackTrace();
                            }

                        }
                    }).start();
                }
            });
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }
}