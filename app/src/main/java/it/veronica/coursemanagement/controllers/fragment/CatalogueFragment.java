package it.veronica.coursemanagement.controllers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.coursemanagement.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import it.veronica.coursemanagement.controllers.RootActivity;
import it.veronica.coursemanagement.model.Course;
import it.veronica.coursemanagement.model.Disponibility;
import it.veronica.coursemanagement.model.Teacher;
import it.veronica.coursemanagement.model.User_type;
import it.veronica.coursemanagement.model.dbManager;
import it.veronica.coursemanagement.tree_holder.ItemHolder;
import it.veronica.coursemanagement.utility.PreferencesManager;

public class CatalogueFragment extends Fragment {

    private View root = null;
    private dbManager  db = null;
    private Context myContext = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_catalogue, container, false);
        myContext = this.getContext();
        db = new dbManager(myContext);
        ((RootActivity) getActivity()).getSupportActionBar().setTitle(R.string.catalogue_page);
        ((RootActivity)getActivity()).getSupportActionBar().show();

        TreeNode tree_root = PopulateTreeView();
        AndroidTreeView tView = new AndroidTreeView(getActivity(), tree_root);
        tView.setDefaultAnimation(true);
        tView.setDefaultViewHolder(ItemHolder.class);
        ConstraintLayout container_view = root.findViewById(R.id.container_view);
        container_view.addView(tView.getView());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean reservationDone = bundle.getBoolean(getResources().getString(R.string.reservation_done), false);
            if (reservationDone) {
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.create_done), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
        return root;
    }

    private TreeNode PopulateTreeView()
    {
        TreeNode tree_root = TreeNode.root();
        int dp_first_node = (int) (getResources().getDimension(R.dimen.first_node) / getResources().getDisplayMetrics().density);
        int dp_second_node = (int) (getResources().getDimension(R.dimen.second_node) / getResources().getDisplayMetrics().density);
        int dp_third_node = (int) (getResources().getDimension(R.dimen.third_node) / getResources().getDisplayMetrics().density);

        Course[] courses = db.GetAllCourse(null);

        for (Course course : courses) {
            TreeNode firstNode = new TreeNode(new ItemHolder.ItemHolderData(course.getTitle(), R.drawable.ic_arrow_forward, dp_first_node));
            Teacher[] teachers = db.GetTeacherByCourseId(course.getId());
            if (teachers.length == 0)
            {
                TreeNode no_teacher = new TreeNode(new ItemHolder.ItemHolderData("Nessun docente disponibile", R.drawable.ic_remove, dp_second_node));
                firstNode.addChild(no_teacher);
            }
            else
            {
                for (Teacher teacher: teachers) {
                    TreeNode secondNode = new TreeNode(new ItemHolder.ItemHolderData(teacher.getFullName(), R.drawable.ic_arrow_forward, dp_second_node));
                    //Per ogni docente, per ogni corso estrapolo le disponibilità
                    Disponibility[] disponibilities = db.GetDisponibilityByTeacherIdCourseId(teacher.getId(), course.getId());
                    if (disponibilities.length == 0)
                    {
                        TreeNode no_disponibility = new TreeNode(new ItemHolder.ItemHolderData("Nessuna data disponibile", R.drawable.ic_remove, dp_third_node));
                        secondNode.addChild(no_disponibility);
                    }
                    else{
                        for (Disponibility disponibility: disponibilities) {
                            String labelText = disponibility.getDatetime() + " - " + disponibility.getSlotTime();
                            TreeNode thirdNode = new TreeNode(new ItemHolder.ItemHolderData(labelText, course, teacher, disponibility, R.drawable.ic_calendar, dp_third_node));
                            thirdNode.setClickListener(thirdNodeListener);
                            secondNode.addChild(thirdNode);
                        }
                    }
                    firstNode.addChild(secondNode);
                }
            }
            tree_root.addChild(firstNode);
        }
        return tree_root;
    }

    private TreeNode.TreeNodeClickListener thirdNodeListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            //Mostro il dettaglio del catalogo
            //Verifico l'utenza
            PreferencesManager preferencesManager = PreferencesManager.getInstance(getResources().getString(R.string.preferencesManager), myContext);
            String user_type_id = preferencesManager.GetPreferenceByKey(getResources().getString(R.string.user_type_id));
            if (user_type_id != null) {
                Fragment catalogueRegistryFragment = new CatalogueRegistryFragment();
                Bundle bundle = new Bundle();
                ItemHolder.ItemHolderData dataValue = (ItemHolder.ItemHolderData) value;
                bundle.putString(getResources().getString(R.string.course_title_course), dataValue.getCourse().getTitle());
                bundle.putString(getResources().getString(R.string.teacher_name_teacher), dataValue.getTeacher().getFullName());
                bundle.putString(getResources().getString(R.string.disponibility_date), dataValue.getDisponibility().getDatetime());
                bundle.putString(getResources().getString(R.string.disponibility_slot), dataValue.getDisponibility().getSlotTime());
                bundle.putInt(getResources().getString(R.string.disponibility_id), dataValue.getDisponibility().getId());
                catalogueRegistryFragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, catalogueRegistryFragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(CatalogueRegistryFragment.class.getName()) // name can be null
                        .commit();
            }
            else{
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.need_registration), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    };
}