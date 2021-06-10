package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

import it.veronica.coursemanagement.adapters.UserAdapter;
import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.User;
import it.veronica.coursemanagement.model.dbManager;

public class UserFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_user, container, false);
        myContext = this.getContext();
        db = new dbManager(myContext);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.user_page);

        User[] users = db.GetAllUser();
        ArrayList<User> list1 = new ArrayList<User>();
        Collections.addAll(list1, users);
        UserAdapter adapter = new UserAdapter(getActivity(), list1);
        ListView listView = root.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter_click_listener);
        if (users.length == 0)
        {
            root.findViewById(R.id.no_result).setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
        else
        {
            root.findViewById(R.id.no_result).setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }

        FloatingActionButton add = root.findViewById(R.id.add);
        add.setOnTouchListener(add_listener);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            boolean deleted = bundle.getBoolean(getResources().getString(R.string.deleted), false);
            if (deleted) {
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.delete_done), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
        return root;
    }

    public AdapterView.OnItemClickListener adapter_click_listener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            User user = (User) adapterView.getItemAtPosition(i);
            //Ricarico il fragment con il nuovo id, per visualizzare i dettagli del docente creato
            Fragment userRegistryFragment = new UserRegistryFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(getResources().getString(R.string.user_id), user.getId());
            userRegistryFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, userRegistryFragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(UserRegistryFragment.class.getName()) // name can be null
                    .commit();
        }
    };

    public View.OnTouchListener add_listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, UserRegistryFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(UserRegistryFragment.class.getName()) // name can be null
                    .commit();
            return true;
        }
    };
}