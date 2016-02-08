package palarax.e_key_card.entities;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.geo.GeoPoint;
import com.backendless.persistence.BackendlessDataQuery;

public class tags
{
    private java.util.Date created;
    private String objectId;
    private String tagName;
    private java.util.Date updated;
    private String ownerId;
    private BackendlessUser owner;
    private GeoPoint lastLocation;

    public java.util.Date getCreated()
    {
        return created;
    }

    public String gettagName()
    {
        return tagName;
    }

    public void settagName(String tagName)
    {
        this.tagName = tagName;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public java.util.Date getUpdated()
    {
        return updated;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public BackendlessUser getOwner()
    {
        return owner;
    }

    public void setOwner( BackendlessUser owner )
    {
        this.owner = owner;
    }

    public GeoPoint getLastLocation()
    {
        return lastLocation;
    }

    public void setLastLocation( GeoPoint lastLocation )
    {
        this.lastLocation = lastLocation;
    }


    public tags save()
    {
        return Backendless.Data.of( tags.class ).save( this );
    }

    public Future<tags> saveAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<tags> future = new Future<tags>();
            Backendless.Data.of( tags.class ).save( this, future );

            return future;
        }
    }

    public void saveAsync( AsyncCallback<tags> callback )
    {
        Backendless.Data.of( tags.class ).save( this, callback );
    }

    public Long remove()
    {
        return Backendless.Data.of( tags.class ).remove( this );
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
            Backendless.Data.of( tags.class ).remove( this, future );

            return future;
        }
    }

    public void removeAsync( AsyncCallback<Long> callback )
    {
        Backendless.Data.of( tags.class ).remove( this, callback );
    }

    public static tags findById( String id )
    {
        return Backendless.Data.of( tags.class ).findById( id );
    }

    public static Future<tags> findByIdAsync( String id )
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<tags> future = new Future<tags>();
            Backendless.Data.of( tags.class ).findById( id, future );

            return future;
        }
    }

    public static void findByIdAsync( String id, AsyncCallback<tags> callback )
    {
        Backendless.Data.of( tags.class ).findById( id, callback );
    }

    public static tags findFirst()
    {
        return Backendless.Data.of( tags.class ).findFirst();
    }

    public static Future<tags> findFirstAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<tags> future = new Future<tags>();
            Backendless.Data.of( tags.class ).findFirst( future );

            return future;
        }
    }

    public static void findFirstAsync( AsyncCallback<tags> callback )
    {
        Backendless.Data.of( tags.class ).findFirst( callback );
    }

    public static tags findLast()
    {
        return Backendless.Data.of( tags.class ).findLast();
    }

    public static Future<tags> findLastAsync()
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<tags> future = new Future<tags>();
            Backendless.Data.of( tags.class ).findLast( future );

            return future;
        }
    }

    public static void findLastAsync( AsyncCallback<tags> callback )
    {
        Backendless.Data.of( tags.class ).findLast( callback );
    }

    public static BackendlessCollection<tags> find( BackendlessDataQuery query )
    {
        return Backendless.Data.of( tags.class ).find( query );
    }

    public static Future<BackendlessCollection<tags>> findAsync( BackendlessDataQuery query )
    {
        if( Backendless.isAndroid() )
        {
            throw new UnsupportedOperationException( "Using this method is restricted in Android" );
        }
        else
        {
            Future<BackendlessCollection<tags>> future = new Future<BackendlessCollection<tags>>();
            Backendless.Data.of( tags.class ).find( query, future );

            return future;
        }
    }

    public static void findAsync( BackendlessDataQuery query, AsyncCallback<BackendlessCollection<tags>> callback )
    {
        Backendless.Data.of( tags.class ).find( query, callback );
    }
}