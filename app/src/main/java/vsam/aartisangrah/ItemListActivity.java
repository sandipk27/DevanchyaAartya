package vsam.aartisangrah;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.support.v7.widget.SearchView;



/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity  {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SimpleItemRecyclerViewAdapter adapter;
    private List<Mantra> mValues;
    private List<Mantra> mSearch;
    private List<Mantra> mAllItems;
    private RecyclerView recyclerView;
    public static List<Mantra> MANTRAS;
    public static Map<String, Mantra> MANTRA_MAP;
    private int COUNT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        MANTRAS = new ArrayList<>();
        MANTRA_MAP = new HashMap<>();

        createDummyItem();
        COUNT= MANTRAS.size();
        for (int i = 0; i < COUNT; i++) {
            addItem(i);
        }
        mAllItems=new ArrayList<>();
        mAllItems.addAll(MANTRAS);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        recyclerView = findViewById(R.id.item_list);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.item_decor)));

        adapter = new SimpleItemRecyclerViewAdapter(this, MANTRAS, mTwoPane );
        //setupRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate( R.menu.menu_main, menu);
        MenuItem myActionMenuItem = menu.findItem( R.id.search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (TextUtils.isEmpty(newText)) {

                    adapter.filter(mAllItems);


                } else {

                    mSearch = new ArrayList<>();

                    if(newText.matches("^[0-9]*$") && newText.length() >= 1)
                        newText=ConvertNumbersToMarathi(newText).trim();

                    if(newText.matches("^[A-Z][a-z]*$"))
                        newText="";

                    for (Mantra s : mAllItems) {
                        if (s.mantraname.contains(newText)) {
                            mSearch.add(s);
                        }
                        else if(s.id.contains(newText)){
                            mSearch.add(s);
                        }
                    }
                    adapter.filter(mSearch);

                }


                return true;
            }

        });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, MANTRAS, mTwoPane));
    }

    private String ConvertNumbersToMarathi(String newText) {
        String str="";
        char[] ch=newText.toCharArray();

        for(int i=0;i<ch.length;i++){
            switch(ch[i]){
                case '1': str=str+"१";break;
                case '2': str=str+"२";break;
                case '3': str=str+"३";break;
                case '4': str=str+"४";break;
                case '5': str=str+"५";break;
                case '6': str=str+"६";break;
                case '7': str=str+"७";break;
                case '8': str=str+"८";break;
                case '9': str=str+"९";break;
                case '0': str=str+"०";break;
            }
        }
        if(str.isEmpty())
            str=newText;
        return str;

    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private List<Mantra> mValues;
        private List<Mantra> mSearch;
        private List<Mantra> mAllItems;

        private final ItemListActivity mParentActivity;
        private final boolean mTwoPane;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mantra item = (Mantra) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                    notifyDataSetChanged();
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<Mantra> items,
                                      boolean twoPane) {
            mValues = items;
            mSearch= new ArrayList<>();
            mSearch.addAll(items);
            mAllItems= new ArrayList<>();
            mAllItems.addAll(items);
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).mantraname);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }


        public void filter(List<Mantra> newList)
        {
            mValues.clear();
            mValues.addAll(newList);
            notifyDataSetChanged();
        }



    }


    public static class Mantra {
        public final String id;
        public final String mantraname;
        public final String detailmantra;

        public Mantra(String id, String content, String details) {
            this.id = id;
            this.mantraname = content;
            this.detailmantra = details;
        }

        @Override
        public String toString() {
            return mantraname;
        }
    }

    private static void addItem(int position) {

        //MANTRA_MAP.put(String.valueOf(position+1), MANTRAS.get(position));
        MANTRA_MAP.put("१", MANTRAS.get(0));
        MANTRA_MAP.put("२", MANTRAS.get(1));
        MANTRA_MAP.put("३", MANTRAS.get(2));
        MANTRA_MAP.put("४", MANTRAS.get(3));
        MANTRA_MAP.put("५", MANTRAS.get(4));
        MANTRA_MAP.put("६", MANTRAS.get(5));
        MANTRA_MAP.put("७", MANTRAS.get(6));
        MANTRA_MAP.put("८", MANTRAS.get(7));


    }

    private static List<Mantra> createDummyItem() {

        /* MANTRAS.add(new Mantra("","","")); */

        MANTRAS.add(new Mantra("१", "श्री गणपती आरती",
                "सुखकर्ता दुखहर्ता वार्ता विघ्नांची\n" +
                        "नुरवी; पुरवी प्रेम कृपा जयाची |\n" +
                        "सर्वांगी सुंदर उटी शेंदुराची,\n" +
                        "कंठी झळके माळ मुक्ताफळांची॥१॥\n" +
                        "\n" +
                        "जय देव जय देव जय मंगलमूर्ती|\n" +
                        "दर्शनमात्रे मनकामना पुरती ॥धृ॥\n" +
                        "\n" +
                        "रत्नखचित फरा तुज गौरीकुमरा|\n" +
                        "चंदनाची उटी कुमकुम केशरा|\n" +
                        "हिरेजडित मुकुट शोभतो बरा |\n" +
                        "रुणझुणती नूपुरे चरणी घागरिया|\n" +
                        "जय देव जय देव जय मंगलमूर्ती ॥२॥\n" +
                        "\n" +
                        "लंबोदर पीतांबर फणिवरबंधना |\n" +
                        "सरळ सोंड वक्रतुंड त्रिनयना|\n" +
                        "दास रामाचा वाट पाहे सदना|\n" +
                        "संकटी पावावे निर्वाणी रक्षावे सुरवरवंदना|\n" +
                        "जय देव जय देव जय मंगलमूर्ती|\n" +
                        "दर्शनमात्रे मनकामना पुरती ॥३॥"));

        MANTRAS.add(new Mantra("२", "श्री दत्त आरती",
                "त्रिगुणात्मक त्रैमूर्ती दत्त हा जाणा\n" +
                        "त्रिगुणी अवतार त्रैलोक्य राणा\n" +
                        "नेति नेति शब्द न ये अनुमाना\n" +
                        "सुरवर मुनिजन योगी समाधी न ये ध्याना ॥१॥\n" +
                        "\n" +
                        "सबाह्य अभ्यंतरी तू एक दत्त\n" +
                        "अभाग्यासी कैसी न कळे ही मात\n" +
                        "पराही परतली तेथे कैचा हा हेत\n" +
                        "जन्म मरणाचा पुरलासे अंत ॥२॥\n" +
                        "\n" +
                        "दत्त येऊनिया उभा ठाकला\n" +
                        "भावे साष्टांगेसी प्रणिपात केला\n" +
                        "प्रसन्न होउनी आशीर्वाद दिधला\n" +
                        "जन्ममरणाचा फेरा चुकविला ॥३॥\n" +
                        "\n" +
                        "दत्त दत्त ऐसे लागले ध्यान\n" +
                        "हरपले मन झाले उन्मन\n" +
                        "मी तू पणाची झाली बोळवण\n" +
                        "एका जनार्दनी श्रीदत्त ध्यान ॥४॥"));

        MANTRAS.add(new Mantra("३", "दुर्गा माता आरती",
                "दुर्गे दुर्गटभारी तुजविण संसारी\n" +
                        "अनाथनाथे अंबे करुणा विस्तारी\n" +
                        "वारी वारी जन्म मरणांतें वारी\n" +
                        "हारी पडलो आता संकट निवारी॥१॥\n" +
                        "\n" +
                        "जय देवी जय देवी जय महिषासुरमथिनी\n" +
                        "सुरवर ईश्वरदे तारक संजीवनी, जय देवी जय देवी ॥धृ॥\n" +
                        "\n" +
                        "त्रिभुवनी भुवनी पाहता तुज ऐसे नाही\n" +
                        "चारी श्रमले परंतु न बोलवे काही\n" +
                        "साही विवाद करता पडले प्रवाही\n" +
                        "ते तू भक्तांलागी पावसि लवलाही॥२॥\n" +
                        "\n" +
                        "जय देवी जय देवी जय महिषासुरमथिनी\n" +
                        "सुरवर ईश्वरदे तारक संजीवनी, जय देवी जय देवी ॥धृ॥\n" +
                        "\n" +
                        "प्रसन्नवदने प्रसन्न होशी निजदासा\n" +
                        "क्लेशापासुन सोडी तोडी भवपाशा\n" +
                        "अंबे तुजवाचून कोण पुरवील आशा\n" +
                        "नरहरि तल्लिन झाला पदपंकजलेशा॥३॥\n" +
                        "\n" +
                        "जय देवी जय देवी जय महिषासुरमथिनी\n" +
                        "सुरवर ईश्वरदे तारक संजीवनी, जय देवी जय देवी ॥धृ॥"));


        MANTRAS.add(new Mantra("४", "श्री शंकराची आरती",
                "लवथवती विक्राळा ब्रह्मांडी माळा,\n" +
                        "वीषे कंठ काळा त्रिनेत्री ज्वाळा\n" +
                        "लावण्य सुंदर मस्तकी बाळा,\n" +
                        "तेथुनिया जळ निर्मळ वाहे झुळझुळा॥१॥\n" +
                        "\n" +
                        "जय देव जय देव जय श्रीशंकरा,\n" +
                        "आरती ओवाळू तुज कर्पुरगौरा जय देव जय देव ॥धृ॥\n" +
                        "\n" +
                        "कर्पुरगौरा भोळा नयनी विशाळा,\n" +
                        "अर्धांगी पार्वती सुमनांच्या माळा\n" +
                        "विभुतीचे उधळण शितकंठ नीळा,\n" +
                        "ऐसा शंकर शोभे उमा वेल्हाळा ॥२॥\n" +
                        "\n" +
                        "जय देव जय देव जय श्रीशंकरा,\n" +
                        "आरती ओवाळू तुज कर्पुरगौरा जय देव जय देव ॥धृ॥\n" +
                        "\n" +
                        "देवी दैत्यी सागरमंथन पै केले,\n" +
                        "त्यामाजी अवचित हळहळ जे उठले\n" +
                        "ते त्वा असुरपणे प्राशन केले,\n" +
                        "नीलकंठ नाम प्रसिद्ध झाले ॥३॥\n" +
                        "\n" +
                        "जय देव जय देव जय श्रीशंकरा,\n" +
                        "आरती ओवाळू तुज कर्पुरगौरा जय देव जय देव ॥धृ॥\n" +
                        "\n" +
                        "व्याघ्रांबर फणिवरधर सुंदर मदनारी,\n" +
                        "पंचानन मनमोहन मुनिजनसुखकारी\n" +
                        "शतकोटीचे बीज वाचे उच्चारी,\n" +
                        "रघुकुलटिळक रामदासा अंतरी॥४॥\n" +
                        "\n" +
                        "जय देव जय देव जय श्रीशंकरा,\n" +
                        "आरती ओवाळू तुज कर्पुरगौरा जय देव जय देव ॥धृ॥"));

        MANTRAS.add(new Mantra("५", "श्री विठ्ठलाची आरती",
                  "युगे अठ्ठावीस विटेवरी उभा\n" +
                        "वामाङ्गी रखुमाईदिसे दिव्य शोभा ।\n" +
                        "पुण्डलिकाचे भेटि परब्रह्म आले गा\n" +
                        "चरणी वाहे भीमा उद्धरी जगा ॥१॥\n" +
                        "\n" +
                        "जय देव जय देव जय पाण्डुरङ्गा ।\n" +
                        "रखुमाई वल्लभा राईच्या वल्लभा पावे जिवलगा ॥धृ०॥\n" +
                        "\n" +
                        "तुळसीमाळा गळा कर ठेऊनी कटी\n" +
                        "कासे पीताम्बर कस्तुरी लल्लाटी ।\n" +
                        "देव सुरवर नित्य येती भेटी\n" +
                        "गरुड हनुमन्त पुढे उभे राहती ॥२॥\n" +
                        "\n" +
                        "धन्य वेणूनाद अणुक्षेत्रपाळा\n" +
                        "सुवर्णाची कमळे वनमाळा गळा ।\n" +
                        "राई रखुमाबाई राणीया सकळा\n" +
                        "ओवाळिती राजा विठोबा सावळा ॥३॥\n" +
                        "\n" +
                        "ओवाळू आरत्या कुरवण्ड्या येती\n" +
                        "चन्द्रभागेमाजी सोडुनिया देती ।\n" +
                        "दिण्ड्या पताका वैष्णव नाचती\n" +
                        "पण्ढरीचा महिमा वर्णावा किती ॥४॥\n" +
                        "\n" +
                        "आषाढी कार्तिकी भक्तजन येती\n" +
                        "चन्द्रभागेमाजी स्नाने जे करिती ।\n" +
                        "दर्शन होळामात्रे तया होय मुक्ति\n" +
                        "केशवासी नामदेव भावे ओवाळिती ॥५॥"));

        MANTRAS.add(new Mantra("६","श्री महालक्ष्मी आरती",
                        "जय देवी जय देवी जय महालक्ष्मी।\n"+
                        "वससी व्यापकरुपे तू स्थूलसूक्ष्मी॥\n"+
                        "करवीरपुरवासिनी सुरवरमुनिमाता।\n"+
                        "पुरहरवरदायिनी मुरहरप्रियकान्ता।\n"+
                        "कमलाकारें जठरी जन्मविला धाता।\n"+
                        "सहस्त्रवदनी भूधर न पुरे गुण गातां॥\n"+
                        "जय देवी जय देवी...॥\n"+
                                "\n" +
                        "मातुलिंग गदा खेटक रविकिरणीं।\n"+
                        "झळके हाटकवाटी पीयुषरसपाणी।\n"+
                        "माणिकरसना सुरंगवसना मृगनयनी।\n"+
                        "शशिकरवदना राजस मदनाची जननी॥\n"+
                        "जय देवी जय देवी...॥\n"+
                                "\n" +
                        "तारा शक्ति अगम्या शिवभजकां गौरी।\n"+
                        "सांख्य म्हणती प्रकृती निर्गुण निर्धारी।\n"+
                        "गायत्री निजबीजा निगमागम सारी।\n"+
                        "प्रगटे पद्मावती निजधर्माचारी॥\n"+
                        "जय देवी जय देवी...॥\n"+
                        "अमृतभरिते सरिते अघदुरितें वारीं।\n"+
                        "मारी दुर्घट असुरां भवदुस्तर तारीं।\n"+
                        "वारी मायापटल प्रणमत परिवारी।\n"+
                        "हें रुप चिद्रूप दावी निर्धारी॥\n"+
                        "जय देवी जय देवी...॥\n"+
                                "\n" +
                        "चतुराननें कुश्चित कर्मांच्या ओळी।\n"+
                        "लिहिल्या असतिल माते माझे निजभाळी।\n"+
                        "पुसोनि चरणातळी पदसुमने क्षाळी।\n"+
                        "मुक्तेश्वर नागर क्षीरसागरबाळी॥\n"+
                        "जय देवी जय देवी...॥"));

        MANTRAS.add(new Mantra("७","श्री रामाची आरती",
                        "त्रिभुवनमंडितमाळ गळां।\n"+
                        "आरती ओवाळूं पाहूं ब्रह्मपुतळा॥\n"+
                        "श्रीराम जय राम जय जय राम।\n"+
                        "आरती ओवाळूं पाहूं सुन्दर मेघश्यामा॥\n"+
                                "\n" +
                        "ठकाराचे ठाण वारीं धनुष्यबाण।\n"+
                        "मारुती सन्मुख उभा कर जोडून॥\n"+
                        "श्रीराम जय राम जय जय राम।\n"+
                        "आरती ओवाळूं पाहूं सुन्दर मेघश्यामा॥\n"+
                                "\n" +
                        "भरत शत्रुघ्न दोघे चामर ढाळिती।\n"+
                        "स्वर्गाहून देव पुष्पवृष्टि करिती॥\n"+
                        "श्रीराम जय राम जय जय राम।\n"+
                        "आरती ओवाळूं पाहूं सुन्दर मेघश्यामा॥\n"+
                                "\n" +
                        "रत्नजडित हार वर्णू काय मुकुटी।\n"+
                        "आरती ओवाळूं चौदां भुवनांच्या कोटी॥\n"+
                        "श्रीराम जय राम जय जय राम।\n"+
                        "आरती ओवाळूं पाहूं सुन्दर मेघश्यामा॥\n"+
                                "\n" +
                        "विष्णुदास नामा म्हणे मागतो तूतें।\n"+
                        "आरती ओंवाळूं पाहूं सीतापतीतें॥\n"+
                        "श्रीराम जय राम जय जय राम।"+
                        "आरती ओवाळूं पाहूं सुन्दर मेघश्यामा॥"));

        MANTRAS.add(new Mantra("८","श्री कृष्णाची आरती",
                        "ओवालू आरती मदनगोपाळा।\n"+
                        "श्यामसुन्दर गळा लं वैजयन्तीमाळा॥\n"+
                        "चरणकमल ज्याचे अति सुकुमार।\n"+
                        "ध्वजवज्रानकुश ब्रीदाचा तोडर॥\n"+
                        "ओवालू आरती मदनगोपाळा...॥\n"+
                                "\n" +
                        "नाभीकमळ ज्याचेब्रह्मयाचे स्थान।\n"+
                        "ह्रीदयीन पदक शोभे श्रीवत्सलांछन॥\n"+
                        "ओवालू आरती मदनगोपाळा...॥\n"+
                        "मुखकमळा पाहता सूर्याचिया कोटी।\n"+
                        "वेधीयेले मानस हारपली धृष्टी॥\n"+
                        "ओवालू आरती मदनगोपाळा...॥\n"+
                                "\n" +
                        "जडित मुगुट ज्याच्या देदीप्यमान।\n"+
                        "तेणे तेजे कोदले अवघे त्रिभुवन॥\n"+
                        "ओवालू आरती मदनगोपाळा...॥\n"+
                                "\n" +
                        "एका जनार्दनी देखियले रूप।\n"+
                        "रूप पाहों जाता झालेसें तद्रूप॥\n"+
                        "ओवालू आरती मदनगोपाळा...॥"));



        return MANTRAS;

    }
}
