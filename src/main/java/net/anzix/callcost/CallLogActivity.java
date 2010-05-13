package net.anzix.callcost;

import net.anzix.callcost.api.World;
import net.anzix.callcost.api.CallRecord;
import net.anzix.callcost.api.Country;
import net.anzix.callcost.api.Plan;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.anzix.callcost.api.CallList;

/**
 *
 * @author elek
 */
public class CallLogActivity extends ListActivity {

    List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    private CallList cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log);

        Country country = World.instance().getCurrentCountry();

        Plan p = country.getPlan(getIntent().getExtras().getString("planid"));
        //p.reset();


        Cursor c = AndroidUtils.getCursor(this);
        startManagingCursor(c);
        cl = AndroidUtils.getCallListFromCursor(c, country.getNumberParser());


        p.getCost(cl);
        setListAdapter(new CallListAdapter(cl));
    }

    class CallListAdapter extends BaseAdapter {

        CallList calllist;

        private final LayoutInflater mInflater;

        public CallListAdapter(CallList cl) {
            this.calllist = cl;
            mInflater = LayoutInflater.from(CallLogActivity.this);
        }

        public int getCount() {
            return calllist.getCalls().size();
        }

        public Object getItem(int arg0) {
            return calllist.getCalls().get(arg0);
        }

        public long getItemId(int arg0) {
            return arg0;
        }

        public View getView(int position, View view, ViewGroup viewgroup) {
            CallRecord cr = calllist.getCalls().get(position);
            if (view == null) {
                view = mInflater.inflate(R.layout.event, null);
            } else {
            }
            ((TextView)view.findViewById(R.id.cost)).setText(cr.getCalculatedCost()+" "+ World.instance().getCurrentCountry().getCurrency());
            ((TextView)view.findViewById(R.id.duration)).setText(cr.getDuration()+" s");
            ((TextView)view.findViewById(R.id.type)).setText(cr.getDestination());
            ((TextView)view.findViewById(R.id.name)).setText(cr.getName());

            return view;
        }
    }
}
