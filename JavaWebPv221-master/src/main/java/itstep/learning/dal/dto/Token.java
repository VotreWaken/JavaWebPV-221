package itstep.learning.dal.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Token {
    private UUID tokenId;
    private UUID userId;
    // за зразком JWT
    // https://en.wikipedia.org/wiki/JSON_Web_Token
    private Date exp;
    private Date iat;

    public Token() {
    }

    public Token( ResultSet res ) throws SQLException {
        setTokenId( UUID.fromString( res.getString("token_id") ) );
        setUserId(  UUID.fromString( res.getString("user_id" ) ) );
        setExp( new Date( res.getTimestamp("exp").getTime() ) );
        setIat( new Date( res.getTimestamp("iat").getTime() ) );
    }

    public UUID getTokenId() {
        return tokenId;
    }

    public void setTokenId(UUID tokenId) {
        this.tokenId = tokenId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    public Date getIat() {
        return iat;
    }

    public void setIat(Date iat) {
        this.iat = iat;
    }
}
