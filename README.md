Android Tools
=============
**Project designed to accelerate the development of android apps.**

<p>
Author: Bruno Jensen (https://github.com/brunojensen)
</p>
<p>
Collaborators: Diego Nunes (https://github.com/odiego), Rafael Garbin (https://github.com/rgarbin)
</p>
## How to use in my android project?

Add br.com.bea.androidtools as Maven dependency:

```xml
...
<dependencies>
...
    <dependency>
        <groupId>br.com.bea.androidtools</groupId>
        <artifactId>br.com.bea.androidtools</artifactId>
        <version>0.0.1</version>
        <type>apklib</type>
    </dependency>
...
</dependencies>
...
```
You also can add as a module in your parent pom:

```xml
<modules>
    <module>br.com.bea.androidtools</module>
</modules>
```


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
## Database Access
**Implementation of a Database using SQlite.**
```java
...
EntityManagerImpl.getInstance().init(this, "DATABASE_NAME", ExampleEntity.class
                                                          , Example2Entity.class
                                                          , Example3Entity.class);
...

EntityManagerImpl.getInstance().<ExampleEntity>find( ... );
EntityManagerImpl.getInstance().<ExampleEntity>persist( ... );
EntityManagerImpl.getInstance().<ExampleEntity>update( ... );
EntityManagerImpl.getInstance().<ExampleEntity>delete( ... );
EntityManagerImpl.getInstance().<ExampleEntity>search( ... );
...

```
## QueryBuilder
**Easy and intuitive way to build your query.**
```java
...
QueryBuilder.select()
            .from(EventEntity.class)
            .where( Restriction.like("name", "Frank", MatchMode.ANYWHERE) )
            .orderBy("id", "name");
...
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

## View: Adapter
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

## View: AsyncTask
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

Please fork this repository and contribute back using pull requests.

