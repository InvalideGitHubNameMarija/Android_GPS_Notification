package com.example.noviprojekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn_pokreni,btn_zaustavi,btn_centar,btn_bolnice, btn_policije;
    TextView koordinate;
    private BroadcastReceiver broadcastReceiver;
    NotificationManagerCompat compat;
    int brojac=0;

    String grad="";

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    String koords=intent.getExtras().get("coordinates")+"";
                    //double latitude=Double.parseDouble(koords.substring(0,18));
                    //double longitude=Double.parseDouble(koords.substring(19,37));

                    int longitude=Integer.parseInt(koords.substring(0,2));
                    int drugi=koords.indexOf('.',3);
                    int latitude=Integer.parseInt(koords.substring(drugi-2,drugi));

                    //int latitude=Integer.parseInt(koords.substring(19,21));

                    checkingCity(latitude,longitude);

                    koordinate.setText(intent.getExtras().get("coordinates")+"");
                    //koordinate.append("\n" +intent.getExtras().get("coordinates"));

                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    private void checkingCity(int latitude, int longitude) {

        if(latitude==44 && longitude==20){
            //Toast.makeText(this, "U Beogradu si", Toast. LENGTH_SHORT).show();
            grad="Beograd";
            //Toast toast = Toast. makeText(getApplicationContext(), grad, Toast. LENGTH_SHORT); toast. show();
        }

        if(latitude==45 && longitude==19){
            grad="Novi Sad";
        }


        if(latitude==43 && longitude==21){
            grad="Nis";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_pokreni=(Button)findViewById(R.id.btnPokreni);
        btn_zaustavi=(Button)findViewById(R.id.btnZaustavi);
        btn_centar=(Button)findViewById(R.id.btnCentar);
        btn_bolnice=(Button)findViewById(R.id.btnBolnica);
        btn_policije=(Button)findViewById(R.id.btnPolicija);

        koordinate=(TextView)findViewById(R.id.txtViewKoordinate);
        compat=NotificationManagerCompat.from(this);


        if(!runtime_permissions())
            enable_buttons();

    }



    private void enable_buttons() {

        btn_pokreni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),GPS_Service.class);
                startService(i);
            }
        });


        btn_zaustavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),GPS_Service.class);
                stopService(i);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            }else {
                runtime_permissions();
            }
        }

    }

    public void prikaziCenar(View view) {

        String tekst;

        switch(grad){

            case "":
                prikaziNeuspesna(view);
                break;
            case "Beograd":
                tekst="Centar grada je Knez Mihailova!";
                prikaziUspesna(view,tekst);
                break;

            case "Novi Sad":
                tekst="Centar grada je Zmaj Jovina!";
                prikaziUspesna(view,tekst);
                break;
            case "Nis":
                tekst="Centar grada je Milana Obrenovica!";
                prikaziUspesna(view,tekst);
                break;
            default:
                Toast.makeText(this,"Moras pokrenuti aplikaciju",Toast.LENGTH_SHORT).show();
        }

    }

    public void prikaziNeuspesna(View view) {
        Notification notification1=new NotificationCompat.Builder(this,NotificationGPS.CHANNELID_1)
                .setContentTitle("Neuspesno")
                .setContentText("Morate prvo pritisnuti dugme POKRENI")
                .setSmallIcon(android.R.drawable.stat_notify_error)
                .build();
        compat.notify(brojac,notification1);
        brojac++;
    }

    public void prikaziUspesna(View view,String tekst) {
        Notification notification2=new NotificationCompat.Builder(this,NotificationGPS.CHANNELID_2)
                .setContentTitle("Grad "+ grad)
                .setContentText(tekst)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .build();
        compat.notify(brojac,notification2);
        brojac++;
    }

    public void prikaziBolnice(View view) {
        String tekst;

        switch(grad){

            case "":
                prikaziNeuspesna(view);
                break;
            case "Beograd":
                tekst="Bolnice su: VMA, Dragise Misovic, KBC Zemun!";
                prikaziUspesna(view,tekst);
                break;

            case "Novi Sad":
                tekst="Bolnice su: Klinicki centar Vojvodina, Parks!";
                prikaziUspesna(view,tekst);
                break;
            case "Nis":
                tekst="Bolnice su: Klinicki centar, Vojna bolnica!";
                prikaziUspesna(view,tekst);
                break;
            default:
                Toast.makeText(this,"Moras pokrenuti aplikaciju",Toast.LENGTH_SHORT).show();
        }

    }

    public void prikaziPoliciju(View view) {
        String tekst;

        switch(grad){

            case "":
                prikaziNeuspesna(view);
                break;
            case "Beograd":
                tekst="Policija je u Bulevaru Mihajla Pupina 2!";
                prikaziUspesna(view,tekst);
                break;

            case "Novi Sad":
                tekst="Policija je u Pap Pavla 46!";
                prikaziUspesna(view,tekst);
                break;
            case "Nis":
                tekst="Policija je u Nade Tomic 3";
                prikaziUspesna(view,tekst);
                break;
            default:
                Toast.makeText(this,"Moras pokrenuti aplikaciju",Toast.LENGTH_SHORT).show();
        }


    }

    public void prikaziSetalista(View view) {
        String tekst;

        switch(grad){

            case "":
                prikaziNeuspesna(view);
                break;
            case "Beograd":
                tekst="Setalista: Kalemegdan, Dunavski kej, Tasmajdan!";
                prikaziUspesna(view,tekst);
                break;

            case "Novi Sad":
                tekst="Setalista: Petrovaradin, Dunavski park!";
                prikaziUspesna(view,tekst);
                break;
            case "Nis":
                tekst="Setalista: Park Cair, Niski kej";
                prikaziUspesna(view,tekst);
                break;
            default:
                Toast.makeText(this,"Moras pokrenuti aplikaciju",Toast.LENGTH_SHORT).show();
        }


    }
}