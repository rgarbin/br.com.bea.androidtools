Android Tools
=======================

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
<h4>for ExampleEntity</h4>
<pre>
<code>public final class ExampleService extends Service&#60;ExampleEntity&#62; {
    private static final Service&#60;ExampleEntity&#62; INSTANCE = new ExampleService();
    
    private ExampleService() {}
    
    public Service&#60;ExampleEntity&#62; getInstance() {
        return INSTANCE;
    }
    
    @Override
    public List<ExampleEntity> search(final ExampleEntity entity) { ... }
    
    @Override
    public ExampleEntity find(final ExampleEntity entity) { ... }
    
    ...
}</code>
</pre>
