package it.veronica.coursemanagement.controllers;

import android.os.Bundle;
import android.text.BoringLayout;
import android.view.MenuItem;

import com.example.coursemanagement.R;

import it.veronica.coursemanagement.controllers.fragment.Catalogue;
import it.veronica.coursemanagement.controllers.fragment.LoginFragment;
import it.veronica.coursemanagement.controllers.fragment.LogoutFragment;
import it.veronica.coursemanagement.controllers.fragment.TeacherFragment;
import it.veronica.coursemanagement.model.User_type;
import it.veronica.coursemanagement.utility.PreferencesManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RootActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ManageExtra();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //Verifico il tipo di utenza
        PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), this);
        String user_type_id = preferencesManager.GetPreferenceByKey("User_type_id");
        if (user_type_id == null) {user_type_id = "0";}
        navigationView.getMenu().clear();
        if (Integer.parseInt(user_type_id) == User_type.STUDENT.getValue()) {
            navigationView.inflateMenu(R.menu.student_menu);
        } else if (Integer.parseInt(user_type_id) == User_type.TEACHER.getValue()) {
            navigationView.inflateMenu(R.menu.teacher_menu);
        } else if (Integer.parseInt(user_type_id) == User_type.ADMIN.getValue()) {
            navigationView.inflateMenu(R.menu.admin_menu);
        } else {
            navigationView.inflateMenu(R.menu.guest_menu);
        }
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard /*, R.id.nav_gallery, R.id.nav_slideshow*/)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    }

    private void ManageExtra()
    {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        else
        {
            Boolean log_out = extras.getBoolean("log_out");
            if (log_out) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.logout_done), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            if (id == R.id.catalogue) {
                //Cambio fragment per il catalogo
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .add(R.id.nav_host_fragment, Catalogue.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(Catalogue.class.getName()) // name can be null
                        .commit();
            }
            else if (id == R.id.teacher)
            {
                //Cambio fragment per la lista docenti
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .add(R.id.nav_host_fragment, TeacherFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(TeacherFragment.class.getName()) // name can be null
                        .commit();
            }
            else if (id == R.id.login){
                //Cambio fragment per il login
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .add(R.id.nav_host_fragment, LoginFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(LoginFragment.class.getName()) // name can be null
                        .commit();
            }
            else if(id == R.id.logout){
                //Cambio fragment per il logout
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out  // popExit
                        )
                        .replace(R.id.nav_host_fragment, LogoutFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(LogoutFragment.class.getName()) // name can be null
                        .commit();
            }
            return true;
        }
    };
}