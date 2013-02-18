Android Tools
=============

Projeto desenvolvido para acelerar o processo de desenvolvimento de softwares para android.

Arquitetura: [@brunojensen](@brunojensen)
Colaboradores: [@rgarbin](@rgarbin) e [@odiego](@odiego).

## Model
**JSON and ORM Mapping**
```java
@Table(name="TB_EXAMPLE")
public final class ExampleEntity extends Entity<Integer> {

    @Metadata("id") // for JSON
    @Id
    @Column(name="CO_EXAMPLE", type="NUMBER")
    private Integer id;

    @Metadata("name_example") // for JSON
    @Column(name="NM_EXAMPLE", type="VARCHAR(200)")
    private String name;
    ...
}
```

## Service
**Implementation of a ExampleService that use JSONCOntext and Proxy to request and manipulate data.**
```java
public final class ExampleService extends Service<ExampleEntity> {
    private static final Service<ExampleEntity> INSTANCE = new ExampleService();
    
    private ExampleService() {}
    
    public Service<ExampleEntity> getInstance() {
        return INSTANCE;
    }
    
    /**
    * Using JSONContext and Proxy
    */
    @Override
    public List<ExampleEntity> search(final ExampleEntity entity) {
        // Create a proxy instance
        final Proxy<JSONArray> appProxy = new AppProxy();
        // Create the result
        final List<ExampleEntity> result = new LinkedList<ExampleEntity>();
        try {
            // Create properties instance with Proxy connection parameters
            final Properties properties = new Properties();
            properties.put("url_connection", "http://localhost:8080/rest/example");
            properties.put("method", "GET");
            // Initiate a connection
            appProxy.connect(properties);
            // Create a JSONContext instance for ExampleEntity
            final JSONContext<ExampleEntity> jsonContext = new JSONContextImpl<ExampleEntity>(ExampleEntity.class);
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
}
```

## Create an Adapter using AbstractListAdapter
**There is a way to create an Adapter using best practices.**
```java
public final class ExampleListAdapter extends AbstractListAdapter<ExampleEntity> {
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
}
```

## Using an AsyncTask using AbstractAsyncTask into a ListActivity
**To take advantage resultCallback feature, create the AsyncTask inside a ListActivity**
```java
public class ExampleActivity extends ListActivity {

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
}
```
## License

This project is released under the [MIT License](http://www.opensource.org/licenses/MIT).

## Contributing

We welcome contributions!

