package pt.isel.ls.core.common.commands.db_queries;

import static pt.isel.ls.core.common.commands.db_queries.PostData.PostDataEnum.PD_DOSENT_EXIST;
import static pt.isel.ls.core.common.commands.db_queries.PostData.PostDataEnum.PD_FAILED;

public class PostData<T> {

    private final T uniqueId;
    private final PostDataEnum pdEnum;
    private final PostDataType pdType;
    private final int errorCode;
    private final String msg;

    public PostData(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
        this.pdEnum = PD_FAILED;
        this.uniqueId = null;
        this.pdType = null;
    }

    PostData(PostDataEnum pdEnum) {
        if (pdEnum != PD_FAILED)
            throw new IllegalArgumentException("PostDataEnum argument is not equal to "+PD_FAILED);
        this.pdEnum = pdEnum;
        this.uniqueId = null;
        this.pdType = null;
        this.errorCode = 0;
        this.msg = "Could not post information.";
    }

    PostData(PostDataEnum pdEnum, PostDataType pdType) {
        if (pdEnum != PD_DOSENT_EXIST)
            throw new IllegalArgumentException("PostDataEnum argument is not equal to "+PD_DOSENT_EXIST);
        this.pdEnum = pdEnum;
        this.uniqueId = null;
        this.pdType = pdType;
        this.errorCode = 0;
        this.msg = "Could not post, because required information <"+pdType.toString() +
                "> was not found in database.";
    }

    PostData(PostDataEnum pdEnum, PostDataType pdType, T uniqueId){
        this.pdEnum = pdEnum;
        this.uniqueId = uniqueId;
        this.pdType = pdType;
        this.errorCode = 0;
        this.msg = null;
    }

    public T getId() {
        return uniqueId;
    }
    public PostDataEnum getPdEnum() {
        return pdEnum;
    }
    public PostDataType getPdType() {
        return pdType;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public enum PostDataEnum {
        PD_OK,              //Posted information with success
        PD_FAILED,          //Failed to post information
        PD_DOSENT_EXIST,    //Required information to complete the requested post, wasnt found in database


        ;
    }

    /**
     * Used only to tell which information that wasnt found in database,
     * when PostDataEnum is PD_DOSENT_EXIST
     */
    public enum PostDataType {
        PDT_CINEMA("Cinema"),
        PDT_THEATER("Theater"),
        PDT_SESSION("Session"),
        PDT_TICKET("Ticket"),
        PDT_MOVIE("Movie"),


        ;

        private final String str;
        PostDataType(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return str;
        }
    }
}
