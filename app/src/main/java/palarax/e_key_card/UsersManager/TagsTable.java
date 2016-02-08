package palarax.e_key_card.UsersManager;

import java.util.Date;

/**
 * @author Ilya Thai
 */
public class TagsTable {

    private String users_tags;
    private Date created;
    private Date updated;

    public String getUsers_tags()
    {
        return users_tags;
    }

    public void setUsers_tags( String Users_tags )
    {
        this.users_tags = Users_tags;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated( Date updated )
    {
        this.updated = updated;
    }
}
