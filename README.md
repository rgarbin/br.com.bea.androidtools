Android Tools
=======================
<h4>Projeto desenvolvido para acelerar o processo de desenvolvimento de softwares para android</h4>
Arquitetura: [@brunojensen](@brunojensen).<br />
Colaboradores: [@rgarbin](@rgarbin) e [@odiego](@odiego).

<h2>Model</h2>
<h4>JSON and ORM Mapping</h4>
<pre>
<code>@Table(name="TB_EXAMPLE")
public final class ExampleEntity extends Entity&#60;Integer&#62; {

    @Metadata("id") // for JSON
    @Id
    @Column(name="CO_EXAMPLE", type="NUMBER")
    private Integer id;

    @Metadata("name_example") // for JSON
    @Column(name="NM_EXAMPLE", type="VARCHAR(200)")
    private String name;
    ...
}</code>
</pre>

<h2>Service</h2>
<h4>Implementation of a ExampleService that use JSONCOntext and Proxy to request and manipulate data.</h4>
<pre>
<code>public final class ExampleService extends Service&#60;ExampleEntity&#62; {
    private static final Service&#60;ExampleEntity&#62; INSTANCE = new ExampleService();
    
    private ExampleService() {}
    
    public Service&#60;ExampleEntity&#62; getInstance() {
        return INSTANCE;
    }
    
    /**
    * Using JSONContext and Proxy
    */
    @Override
    public List<ExampleEntity> search(final ExampleEntity entity) {
        // Create a proxy instance
        final Proxy&#60;JSONArray&#62; appProxy = new AppProxy();
        // Create the result
        final List&#60;ExampleEntity&#62; result = new LinkedList&#60;ExampleEntity&#62;();
        try {
            // Create properties instance with Proxy connection parameters
            final Properties properties = new Properties();
            properties.put("url_connection", "http://localhost:8080/rest/example");
            properties.put("method", "GET");
            // Initiate a connection
            appProxy.connect(properties);
            // Create a JSONContext instance for ExampleEntity
            final JSONContext&#60;ExampleEntity&#62; jsonContext = new JSONContextImpl&#60;ExampleEntity&#62;(ExampleEntity.class);
            // Send a request to the appProxy which return a List of JSONArray
            for (JSONArray array : appProxy.request(null))
                result.addAll(jsonContext.unmarshal(array)); // Unmarshal each JSONArray into a Collection 
                                                             // of ExampleEntity
        } catch (java.net.ConnectException e) {
            throw new ServiceException(e);
        } finally {
            appProxy.close();
        }
        return result;    
    }
    
    @Override
    public ExampleEntity find(final ExampleEntity entity) { ... }
    
    ...
}</code>
</pre>

<h2>Create an Adapter using AbstractListAdapter</h2>
<h4>There is a way to create an Adapter using best practices.</h4>
<pre>
<code>public final class ExampleListAdapter extends AbstractListAdapter&#60;ExampleEntity&#62; {
    public ExampleListAdapter(final LayoutInflater inflater) {
        super(inflater);
    }

    // Inner class to implement the Holder Pattern
    class ViewHolder {
        TextView label;
    }
    
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        RelativeLayout layout = (RelativeLayout) convertView;
        ViewHolder holder;
        if (layout == null) {
            layout = (RelativeLayout) super.inflater.inflate(R.layout.home, null);
            holder = new ViewHolder();
            holder.label = (TextView) layout.findViewById(R.id.textView);
            layout.setTag(holder);
        } else {
            holder = (ViewHolder) layout.getTag();
        }
        holder.label.setText(getItem(position).getName());
        return layout;
    }
}</code>
</pre>

<h2>Using an AsyncTask using AbstractAsyncTask into a ListActivity</h2>
<h3>To take advantage resultCallback feature, create the AsyncTask inside a ListActivity</h3>
<pre>
<code>public class ExampleActivity extends ListActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ExampleListAdapter listAdapter = 
                        new ExampleListAdapter((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        new AbstractAsyncTask<EventEntity>(EventService.getInstance()) {
            @Override
            public void resultCallback(final List<EventEntity> result) {
                listAdapter.addAll(result);
                setListAdapter(listAdapter);
            }
        }.execute();
    }
}</code>
</pre>



