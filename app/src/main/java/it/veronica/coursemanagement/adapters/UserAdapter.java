package it.veronica.coursemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.coursemanagement.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import it.veronica.coursemanagement.model.Course;
import it.veronica.coursemanagement.model.User;
import it.veronica.coursemanagement.model.User_type;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);
        }

        // Lookup view for data population
        TextView name = (TextView)convertView.findViewById(R.id.name);
        Chip chip = (Chip)convertView.findViewById(R.id.chip);
        TextView email = (TextView)convertView.findViewById(R.id.email);
        // Populate the data into the template view using the data object
        name.setText(user.getFullName());
        if (user.getUser_type_id() == User_type.STUDENT.getValue()) {
            chip.setChipIcon(getContext().getResources().getDrawable(R.drawable.ic_customer));
        }
         else if (user.getUser_type_id() == User_type.TEACHER.getValue()) {
            chip.setChipIcon(getContext().getResources().getDrawable(R.drawable.ic_coaching));
        }
         else
        {
                chip.setChipIcon(getContext().getResources().getDrawable(R.drawable.ic_admin));
        }
        email.setText(user.getEmail());
        // Return the completed view to render on screen
        return convertView;

    }
}