package com.example.coursemanagement.controllers;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.coursemanagement.R;
import com.example.coursemanagement.model.User_type;
import com.example.coursemanagement.utility.PreferencesManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RootActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //Verifico il tipo di utenza
        PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), this);
        String user_type_id = preferencesManager.GetPreferenceByKey("User_type_id");
        navigationView.getMenu().clear();
        if (Integer.parseInt(user_type_id) == User_type.STUDENT.getValue())
        {
            navigationView.inflateMenu(R.menu.student_menu);
        }
        else if(Integer.parseInt(user_type_id) == User_type.TEACHER.getValue())
        {
            navigationView.inflateMenu(R.menu.teacher_menu);
        }
        else if(Integer.parseInt(user_type_id) == User_type.ADMIN.getValue())
        {
            navigationView.inflateMenu(R.menu.admin_menu);
        }
        else
        {
            navigationView.inflateMenu(R.menu.student_menu);
        }
        navigationView.setNavigationItemSelectedListener(this);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard /*, R.id.nav_gallery, R.id.nav_slideshow*/)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.catalogue) {
            //Cambio fragment per il login
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, Catalogue.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name") // name can be null
                    .commit();
        }
        return true;
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}