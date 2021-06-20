package it.veronica.coursemanagement.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.coursemanagement.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import it.veronica.coursemanagement.model.Disponibility;
import it.veronica.coursemanagement.model.Reservation;

public class ReservationCalendarAdapter extends ArrayAdapter<Reservation> {

    private Context myContext;

    public ReservationCalendarAdapter(Context context, ArrayList<Reservation> reservations) {
        super(context, 0, reservations);
        myContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Reservation reservation = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_reservation_calendar, parent, false);
        }

        // Lookup view for data population
        TextView date = convertView.findViewById(R.id.date);
        date.setText(reservation.getDisponibility().getSlotTime());
        TextView title = convertView.findViewById(R.id.title);
        title.setText(reservation.getDisponibility().getCourse().getTitle());

        TextView status = convertView.findViewById(R.id.status);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat2 = null;
        Date reservationTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dateFormat2 = new SimpleDateFormat("dd/MM/yyyy hh:mm");

            try {
                reservationTime = dateFormat2.parse(reservation.getDisponibility().getDatetime() + " " + reservation.getDisponibility().getStartTime());
            } catch (ParseException e) {
            }
        }
        if (reservationTime.getTime() <= currentTime.getTime() && currentTime.getDate() == currentTime.getDate())
        {
            status.setText("Effettuata");
            reservation.setEditable(false);
        }
        else if (reservation.getDeleted() == 1)
        {
            status.setText("Disdetta");
            reservation.setEditable(false);
        }
        else
        {
            status.setText("Attiva");
            reservation.setEditable(true);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}