package it.veronica.coursemanagement.tree_holder;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.coursemanagement.R;
import com.unnamed.b.atv.model.TreeNode;

import it.veronica.coursemanagement.model.Course;
import it.veronica.coursemanagement.model.Disponibility;
import it.veronica.coursemanagement.model.Teacher;

public class ItemHolder extends TreeNode.BaseNodeViewHolder<ItemHolder.ItemHolderData> {
    private TextView tvValue;
    private ImageView arrowView;
    private Context myContext;

    public ItemHolder(Context context){
        super(context);
        myContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View createNodeView(final TreeNode node, ItemHolderData data) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.tree_course, null, false);
        tvValue = (TextView) view.findViewById(R.id.nodeValue);
        tvValue.setText(data.text);

        final ImageView iconView = (ImageView) view.findViewById(R.id.arrowView);
        iconView.setImageDrawable(myContext.getResources().getDrawable(data.icon));
        arrowView = (ImageView) view.findViewById(R.id.arrowView);

        LinearLayout container = view.findViewById(R.id.container);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(data.margin_left,20,0,0);
        container.setLayoutParams(params);
        return view;
    };

    @Override
    public void toggle(boolean active) {
        arrowView.setImageDrawable(context.getResources().getDrawable(active ? R.drawable.ic_arrow_down : R.drawable.ic_arrow_forward));
    }

    public static class ItemHolderData {
        public String text;
        public Course course;
        public Teacher teacher;
        public Disponibility disponibility;
        public int icon;
        public int margin_left;

        public ItemHolderData(String text, int icon, int margin_left) {
            this.text = text;
            this.icon = icon;
            this.margin_left = margin_left;
        }

        public ItemHolderData(String text, Course course, Teacher teacher, Disponibility disponibility, int icon, int margin_left) {
            this.text = text;
            this.icon = icon;
            this.margin_left = margin_left;
            this.course = course;
            this.teacher = teacher;
            this.disponibility = disponibility;
        }

        //#region GETTER AND SETTER

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Course getCourse() {
            return course;
        }

        public void setCourse(Course course) {
            this.course = course;
        }

        public Teacher getTeacher() {
            return teacher;
        }

        public void setTeacher(Teacher teacher) {
            this.teacher = teacher;
        }

        public Disponibility getDisponibility() {
            return disponibility;
        }

        public void setDisponibility(Disponibility disponibility) {
            this.disponibility = disponibility;
        }

        //#endregion
    }
}
