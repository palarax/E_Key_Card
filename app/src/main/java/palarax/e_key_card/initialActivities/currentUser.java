package palarax.e_key_card.initialActivities;

import com.backendless.BackendlessUser;

/**
 * @author Ilya Thai
 * might be userful later on
 */
public class currentUser {

    BackendlessUser user;

    public currentUser (BackendlessUser user){
        this.user = user;
    }

    public void setNewUser(BackendlessUser user)
    {
        this.user = user;
    }

    public BackendlessUser getUser()
    {
        return user;
    }

}
