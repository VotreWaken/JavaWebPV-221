package itstep.learning.dal.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Role {
    private UUID Id;
    private String roleName;
    private boolean canCreate;
    private boolean canDelete;
    private boolean canEdit;
    private boolean canRead;
    private boolean canBan;
    private boolean canBlock;

    public Role(ResultSet res) throws SQLException {
        String id;
        try{id = res.getString("role_id"); }
        catch (Exception ignore){ id = res.getString("id");}
        setId(UUID.fromString(id));
        setRoleName(res.getString("role_name"));
        setCanCreate(res.getBoolean("can_create"));
        setCanDelete(res.getBoolean("can_delete"));
        setCanEdit(res.getBoolean("can_edit"));
        setCanRead(res.getBoolean("can_read"));
        setCanBan(res.getBoolean("can_ban"));
        setCanBlock(res.getBoolean("can_block"));

    }

    public UUID getId() {
        return Id;
    }

    public Role setId(UUID id) {
        Id = id;
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public Role setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public boolean isCanCreate() {
        return canCreate;
    }

    public Role setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
        return this;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public Role setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
        return this;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public Role setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
        return this;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public Role setCanRead(boolean canRead) {
        this.canRead = canRead;
        return this;
    }

    public boolean isCanBan() {
        return canBan;
    }

    public Role setCanBan(boolean canBan) {
        this.canBan = canBan;
        return this;
    }

    public boolean isCanBlock() {
        return canBlock;
    }

    public Role setCanBlock(boolean canBlock) {
        this.canBlock = canBlock;
        return this;
    }
}
