package org.tl.user.DTO;

import java.io.Serializable;

public class CodeCheckDTO implements Serializable {
    private boolean success;
    private String decs;

    public CodeCheckDTO() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }

    public CodeCheckDTO(boolean success, String decs) {
        this.success = success;
        this.decs = decs;
    }

    @Override
    public String toString() {
        return "CodeCheckDTO{" +
                "success=" + success +
                ", decs='" + decs + '\'' +
                '}';
    }
}
