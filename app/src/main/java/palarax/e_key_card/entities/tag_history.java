package palarax.e_key_card.entities;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.geo.GeoPoint;
import com.backendless.persistence.BackendlessDataQuery;

public class tag_history
{
    private java.util.Date updated;
    private String objectId;
    private String ownerId;
    private java.util.Date created;
    private tags tagID;
    private GeoPoint scan_location;
    public java.util.Date getUpdated()
    {
        return updated;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public java.util.Date getCreated()
    {
        return created;
    }

    public tags getTagID()
    {
        return tagID;
    }

    public void setTagID( tags tagID )
    {
        this.tagID = tagID;
    }

    public GeoPoint getScan_location()
    {
        return scan_location;
    }

    public void setScan_location( GeoPoint scan_location )
    {
        this.scan_location = scan_location;
    }


    public tag_history save()
    {
        return Backendless.Data.of( tag_history.class ).save( this );
    }

    public Future<tag_history> saveAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<tag_history> future = new Future<tag_history>();
            Backendless.Data.of( tag_history.class ).save( this, future );

            return future;
        }
    }

    public void saveAsync( AsyncCallback<tag_history> callback )
    {
        Backendless.Data.of( tag_history.class ).save( this, callback );
    }

    public Long remove()
    {
        return Backendless.Data.of( tag_history.class ).remove( this );
    }

    public Future<Long> removeAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<Long> future = new Future<Long>();
            Backendless.Data.of( tag_history.class ).remove( this, future );

            return future;
        }
    }

    public void removeAsync( AsyncCallback<Long> callback )
    {
        Backendless.Data.of( tag_history.class ).remove( this, callback );
    }

    public static tag_history findById( String id )
    {
        return Backendless.Data.of( tag_history.class ).findById( id );
    }

    public static Future<tag_history> findByIdAsync( String id )
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<tag_history> future = new Future<tag_history>();
            Backendless.Data.of( tag_history.class ).findById( id, future );

            return future;
        }
    }

    public static void findByIdAsync( String id, AsyncCallback<tag_history> callback )
    {
        Backendless.Data.of( tag_history.class ).findById( id, callback );
    }

    public static tag_history findFirst()
    {
        return Backendless.Data.of( tag_history.class ).findFirst();
    }

    public static Future<tag_history> findFirstAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<tag_history> future = new Future<tag_history>();
            Backendless.Data.of( tag_history.class ).findFirst( future );

            return future;
        }
    }

    public static void findFirstAsync( AsyncCallback<tag_history> callback )
    {
        Backendless.Data.of( tag_history.class ).findFirst( callback );
    }

    public static tag_history findLast()
    {
        return Backendless.Data.of( tag_history.class ).findLast();
    }

    public static Future<tag_history> findLastAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<tag_history> future = new Future<tag_history>();
            Backendless.Data.of( tag_history.class ).findLast( future );

            return future;
        }
    }

    public static void findLastAsync( AsyncCallback<tag_history> callback )
    {
        Backendless.Data.of( tag_history.class ).findLast( callback );
    }

    public static BackendlessCollection<tag_history> find( BackendlessDataQuery query )
    {
        return Backendless.Data.of( tag_history.class ).find( query );
    }

    public static Future<BackendlessCollection<tag_history>> findAsync( BackendlessDataQuery query )
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<BackendlessCollection<tag_history>> future = new Future<BackendlessCollection<tag_history>>();
            Backendless.Data.of( tag_history.class ).find( query, future );

            return future;
        }
    }

    public static void findAsync( BackendlessDataQuery query, AsyncCallback<BackendlessCollection<tag_history>> callback )
    {
        Backendless.Data.of( tag_history.class ).find( query, callback );
    }
}