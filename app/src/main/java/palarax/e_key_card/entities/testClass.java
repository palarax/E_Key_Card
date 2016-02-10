package palarax.e_key_card.entities;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.Iterator;

/**
 * Created by Ithai on 8/02/2016.
 */
public class testClass {


    public void loginUserAndGetProperties()
    {

        Backendless.Data.of( BackendlessUser.class ).find( new AsyncCallback<BackendlessCollection<BackendlessUser>>()
        {
            @Override
            public void handleResponse( BackendlessCollection<BackendlessUser> users )
            {
                Iterator<BackendlessUser> userIterator = users.getCurrentPage().iterator();

                while( userIterator.hasNext() )
                {
                    BackendlessUser user = userIterator.next();
                    System.out.println( "Email - " + user.getEmail() );
                    System.out.println( "User ID - " + user.getUserId() );
                    System.out.println( "Phone Number - " + user.getProperty( "tag" ) );
                    System.out.println( "============================" );
                }
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
                System.out.println( "Server reported an error - " + backendlessFault.getMessage() );
            }
        } );
    }

    private void getFromTable()
    {
        AsyncCallback<BackendlessCollection<tags>> callback=new AsyncCallback<BackendlessCollection<tags>>()
        {
            @Override
            public void handleResponse( BackendlessCollection<tags> restaurants )
            {
                System.out.println( "Loaded " + restaurants.getCurrentPage().size() + "tags objects" );
                System.out.println( "Total tags in the Backendless storage - " + restaurants.getTotalObjects() );

                Iterator<tags> iterator=restaurants.getCurrentPage().iterator();

                while( iterator.hasNext() )
                {
                    tags restaurant=iterator.next();
                    System.out.println( "Restaurant name = " + restaurant.getTagName() );
                }

            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {

            }
        };

        Backendless.Data.of( tags.class ).find( callback );

    }


    //cycles through tags and finds users
    private void findUserTags(final String objectID)
    {
        Backendless.Persistence.of(tags.class).find(new AsyncCallback<BackendlessCollection<tags>>() {
            @Override
            public void handleResponse(BackendlessCollection<tags> foundtags) {
                Iterator<tags> tagIterator = foundtags.getCurrentPage().iterator();
                String s = "";
                while (tagIterator.hasNext()) {
                    tags tag = tagIterator.next();
                    System.out.println("owner: "+objectID);
                    System.out.println("owner: "+tag.getTagName());
                    if(tag.getOwnerId() != null) {
                        if (tag.getOwnerId().equals(objectID)) {
                            s += tag.getTagName() + " ";
                        }
                    }
                }
                //Toast.makeText(getContext(), "user owns: " + s, Toast.LENGTH_LONG).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        });
    }

    public void cycleThroughUsers() {
        Backendless.Data.of(BackendlessUser.class).find(new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> users) {
                Iterator<BackendlessUser> userIterator = users.getCurrentPage().iterator();

                while (userIterator.hasNext()) {
                    BackendlessUser user = userIterator.next();
                    System.out.println("Email - " + user.getEmail());
                    System.out.println("User ID - " + user.getUserId());
                    System.out.println("Phone Number - " + user.getProperty("phoneNumber"));
                    System.out.println("============================");
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                System.out.println("Server reported an error - " + backendlessFault.getMessage());
            }
        });
    }
}
