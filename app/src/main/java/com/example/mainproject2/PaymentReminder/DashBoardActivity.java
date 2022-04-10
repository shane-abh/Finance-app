package com.example.mainproject2.PaymentReminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mainproject2.ExpenseManager.ExpenseCalculatorMain;
import com.example.mainproject2.ProfileActivity;
import com.example.mainproject2.R;
import com.example.mainproject2.StockPlatform.MyPortfolioActivity;
import com.example.mainproject2.StockPlatform.RealTimePriceAlert;
import com.example.mainproject2.StockPlatform.Search;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class DashBoardActivity extends AppCompatActivity implements TasksFragment.OnFragmentInteractionListener {

    private final String TAG = "DashBoardActivity";



    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    NavigationView nav_view;

    private TaskListAdapter taskListAdapter;
    private Task currentTask;

    private final int NEW_ACTIVITY = 0;
    private final int EDIT_ACTIVITY = 1;

    private final int CONTEXT_MENU_EDIT = 0;
    private final int CONTEXT_MENU_DELETE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        Log.d(TAG, "starting dashboard activity");

        taskListAdapter = new TaskListAdapter(this);
        currentTask = null;

        drawerLayout = (DrawerLayout)findViewById(R.id.activity_main);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Payment Reminder");

        nav_view = findViewById(R.id.nav_view);
        Navigation_drawer();

        switchFragment(TasksFragment.class);

    }

    public void Navigation_drawer(){
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_Home:
                        Intent intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent1);
                        return true;

                    case R.id.nav_search_stock:
                        Intent intent2 = new Intent(getApplicationContext(), Search.class);
                        startActivity(intent2);

                    case R.id.nav_portfolio:
                        Intent intent3 = new Intent(getApplicationContext(), MyPortfolioActivity.class);
                        startActivity(intent3);
                        return true;

                    case R.id.nav_expense:
                        Intent intent4 = new Intent(getApplicationContext(), ExpenseCalculatorMain.class);
                        startActivity(intent4);
                        return true;

                    case R.id.nav_priceAlert:
                        Intent intent5 = new Intent(getApplicationContext(), RealTimePriceAlert.class);
                        startActivity(intent5);
                        return true;

                    case R.id.nav_reminder:
                        Intent intent6 = new Intent(getApplicationContext(), DashBoardActivity.class);
                        startActivity(intent6);
                        return true;


                }
                return true;
            }
        });
    }


    void switchFragment(Class fragmentClass){
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.tasksfragment, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public TaskListAdapter getTaskListAdapter(){
        return taskListAdapter;
    }

    public void onFragmentInteraction(int position){
        Intent intent = new Intent(getBaseContext(), EditActivity.class);

        currentTask = taskListAdapter.getItem(position);
        currentTask.toIntent(intent);
        DashBoardActivity.this.startActivityForResult(intent, EDIT_ACTIVITY);
    }

    public void onAddClick(View view)
    {
        Intent intent = new Intent(getBaseContext(), EditActivity.class);

        currentTask = new Task();
        currentTask.toIntent(intent);

        DashBoardActivity.this.startActivityForResult(intent, NEW_ACTIVITY);
    }

    public void onEditClick(View view){
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent().getParent();
        final int position = listView.getPositionForView(parentRow);

        Intent intent = new Intent(getBaseContext(), EditActivity.class);
        currentTask = taskListAdapter.getItem(position);
        currentTask.toIntent(intent);
        startActivityForResult(intent, EDIT_ACTIVITY);
    }

    public void onDeleteClick(View view){
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent().getParent();
        final int position = listView.getPositionForView(parentRow);
        taskListAdapter.delete(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_ACTIVITY || requestCode == EDIT_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                currentTask.fromIntent(data);
                if (requestCode == NEW_ACTIVITY)
                    taskListAdapter.add(currentTask);
                else
                    taskListAdapter.update(currentTask);
            }
            currentTask = null;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.task_list)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

            menu.setHeaderTitle(taskListAdapter.getItem(info.position).getName());
            menu.add(Menu.NONE, CONTEXT_MENU_EDIT, Menu.NONE, "Edit");
            menu.add(Menu.NONE, CONTEXT_MENU_DELETE, Menu.NONE, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = item.getItemId();

        if (index == CONTEXT_MENU_EDIT)
        {
            Intent intent = new Intent(getBaseContext(), EditActivity.class);

            currentTask = taskListAdapter.getItem(info.position);
            currentTask.toIntent(intent);
            startActivityForResult(intent, EDIT_ACTIVITY);
        }
        else if (index == CONTEXT_MENU_DELETE)
        {
            taskListAdapter.delete(info.position);
        }

        return true;
    }

    private AdapterView.OnItemClickListener listOnItemClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Intent intent = new Intent(getBaseContext(), EditActivity.class);

            currentTask = taskListAdapter.getItem(position);
            currentTask.toIntent(intent);
            DashBoardActivity.this.startActivityForResult(intent, EDIT_ACTIVITY);
        }
    };
}

class TaskListAdapter extends BaseAdapter {

    private final String TAG = "TaskListAdapter";

    static class ViewHolder
    {
        TextView name;
        TextView time;
        TextView date;
        TextView category,amount,repeat;
    }

    private Context context;
    private DAO dao; // access to data
    private LayoutInflater inflater;

    private AlarmManager alarmManager;

    public TaskListAdapter (Context context)
    {
        Log.d(TAG, "calling constructor, context is " + (context!=null ? "not null" : "null"));

        this.context = context;
        dao = DAO.getInstance(context);

        inflater = LayoutInflater.from(context);

        alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        Task task = dao.get(position);

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.record, null);

            holder = new ViewHolder();
            holder.name = (TextView)convertView.findViewById(R.id.record_name);
            holder.time = (TextView)convertView.findViewById(R.id.record_time);
            holder.date = (TextView)convertView.findViewById(R.id.record_date);
            holder.amount = convertView.findViewById(R.id.record_amount);
            holder.repeat = convertView.findViewById(R.id.record_repeat);
            holder.category = convertView.findViewById(R.id.Category);



            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.name.setText(task.getName());
        holder.time.setText(DAO.formatTime(task));
        holder.date.setText(DAO.formatDate(task));
        holder.category.setText(task.getCategory());
        holder.amount.setText(String.valueOf(task.getAmount()));
        holder.repeat.setText(task.getRepeat());

        if (task.getOutdated() || !task.getEnabled())
            holder.name.setTextColor(context.getResources().getColor(R.color.outdated));
        else
            holder.name.setTextColor(context.getResources().getColor(R.color.active));

        return convertView;
    }

    public int getCount()
    {
        return dao.size();
    }

    public Task getItem(int position)
    {
        return dao.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public void add(Task Task)
    {
        dao.add(Task);
        update();
    }

    public void delete(int index)
    {
        cancelTask(dao.get(index));
        dao.remove(index);
        update();
    }

    public void update(Task Task)
    {
        dao.update(Task);
        update();
    }

    private void update()
    {
        for (int i = 0; i < dao.size(); i++)
            setTask(dao.get(i));

        notifyDataSetChanged();
    }

    private long getDuration(){
        // get todays date
        Calendar cal = Calendar.getInstance();
        // get current month
        int currentMonth = cal.get(Calendar.MONTH);

        // move month ahead
        currentMonth++;
        // check if has not exceeded threshold of december

        if(currentMonth > Calendar.DECEMBER){
            // alright, reset month to jan and forward year by 1 e.g fro 2013 to 2014
            currentMonth = Calendar.JANUARY;
            // Move year ahead as well
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
        }

        // reset calendar to next month
        cal.set(Calendar.MONTH, currentMonth);
        // get the maximum possible days in this month
        int maximumDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // set the calendar to maximum day (e.g in case of fEB 28th, or leap 29th)
        cal.set(Calendar.DAY_OF_MONTH, maximumDay);
        long thenTime = cal.getTimeInMillis(); // this is time one month ahead



        return (thenTime); // this is what you set as trigger point time i.e one month after

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setTask(Task task){
        PendingIntent sender;
        Intent intent;

        Log.d(TAG, "Setting task to alarm at " + task.getDate() + (task.getEnabled() ? " enabled" : "") + (task.getOutdated() ? " outdated" : ""));

        if (task.getEnabled() && !task.getOutdated())
        {
            intent = new Intent(context, TaskReceiver.class);
            task.toIntent(intent);
            sender = PendingIntent.getBroadcast(context, (int)task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.getDate(), sender);


            switch (task.getRepeat()){
                case "Monthly": alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, task.getDate(),
                        getDuration(), sender);

                case "Week": alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, task.getDate(),
                        1000 * 10080 *60 * 1, sender);

                case "Daily": alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, task.getDate(),
                        AlarmManager.INTERVAL_DAY, sender);

                case "None" :
                    Toast.makeText(context, task.getRepeat().toString(), Toast.LENGTH_LONG);


            }

//            if(task.getRepeat().toString()=="Monthly"){
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, task.getDate(),
//                        1000 * 10 * 1, sender);
//                Toast.makeText(context, task.getRepeat().toString(), Toast.LENGTH_LONG);
//                System.out.println("Testing1: "+task.getRepeat());
//            }

        }
    }

    private void cancelTask(Task task){
        PendingIntent sender;
        Intent intent;
        intent = new Intent(context, TaskReceiver.class);
        task.toIntent(intent);
        sender = PendingIntent.getBroadcast(context, (int)task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(sender);
    }
}

