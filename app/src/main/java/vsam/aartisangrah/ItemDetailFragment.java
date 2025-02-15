package vsam.aartisangrah;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ItemListActivity.Mantra mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = ItemListActivity.MANTRA_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.mantraname);
                //appBarLayout.setExpandedTitleColor(0x8eff60);
            }

            if(mItem.id.equals("१")) {
                ((ImageView) activity.findViewById(R.id.profile_image)).setImageResource(R.mipmap.ganesha);
            }
            else if(mItem.id.equals("२")) {
                ((ImageView) activity.findViewById(R.id.profile_image)).setImageResource(R.mipmap.shridatt);
            }
            else if(mItem.id.equals("३")) {
                ((ImageView) activity.findViewById(R.id.profile_image)).setImageResource(R.mipmap.durgamata);
            }
            else if(mItem.id.equals("४")) {
                ((ImageView) activity.findViewById(R.id.profile_image)).setImageResource(R.mipmap.shiva);
            }
            else if(mItem.id.equals("५")) {
                ((ImageView) activity.findViewById(R.id.profile_image)).setImageResource(R.mipmap.vitthala);
            }
            else if(mItem.id.equals("६")) {
                ((ImageView) activity.findViewById(R.id.profile_image)).setImageResource(R.mipmap.shrimahalakshmi);
            }
            else if(mItem.id.equals("७")) {
                ((ImageView) activity.findViewById(R.id.profile_image)).setImageResource(R.mipmap.raam);
            }
            else if(mItem.id.equals("८")) {
                ((ImageView) activity.findViewById(R.id.profile_image)).setImageResource(R.mipmap.krishna);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.detailmantra);
        }



        return rootView;
    }
}
