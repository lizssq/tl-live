package org.tl.user.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnreadCountDTO implements java.io.Serializable {
    private int privateChat;
    private int systemNotice;
    
    @JsonIgnore
    public int getTotal() {
        return privateChat + systemNotice;
    }
}