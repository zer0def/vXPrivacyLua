package eu.faircode.xlua.api.xstandard.interfaces;

import android.os.Bundle;

import eu.faircode.xlua.AppGeneric;
import eu.faircode.xlua.api.xstandard.database.SqlQuerySnake;

public interface IUserPacket {
    /**
     * Write the User information (User ID & Category) using a more exposed function.
     * <p>
     * This function is used to circumvent the use of {@code Super.toBundle()} as, even when cast to the lower object, it invokes the object that inherits it.
     * </p>
     * @param b Bundle to which the User Information is to be written.
     * @return The same bundle that was passed in to the parameter, facilitating chained calls.
     */
    Bundle writePacketUserBundle(Bundle b);

    /**
     * Read the User Information from the Bundle (User ID & Category) and write it to the Base Object.
     * <p>
     * This function is utilized to avoid using {@code Super.fromBundle()} as, even when casted to the lower object, it invokes the object that inherits it.
     * </p>
     * @param b Bundle from which to read the User Information (User ID & Category).
     * @return void
     */
    void readPacketUserBundle(Bundle b);

    /**
     * Write the Header Portion of the Packet to the Bundle.
     * <p>
     * Due to functions like fromParcel(), it's recommended to write/read such data after all other data is read/written
     * to avoid any issues with the Order of Things as this is the last (optional data) to write.
     * </p>
     * <p>
     * Here are some items that can be written if filled out. If any of the data is not specified or is NULL, then it will not be written and will use default values instead:
     * <ul>
     *   <li><b>Code:</b> Command Code helps handle sub actions. Default is (CODE_NULL_EMPTY).</li>
     *   <li><b>Kill:</b> Kill Object like application if set. Default is (false).</li>
     *   <li><b>Key:</b> Secret key to use to authenticate communication.</li>
     * </ul>
     * </p>
     * @param b Bundle to write the Header to (Code, Kill, key)
     * @return void
     */
    Bundle writePacketHeaderBundle(Bundle b);

    /**
     * Read the Header Portion of the Packet from the Bundle.
     * <p>
     * Due to functions like fromParcel(), it's recommended to write/read such data after all other data is read/written
     * to avoid any issues with the Order of Things as this is the last (optional data) to write.
     * </p>
     * <p>
     * Here are some items that can be read if filled out:
     * <ul>
     *   <li><b>Code:</b> Command Code help handle sub actions. Default is (CODE_NULL_EMPTY).</li>
     *   <li><b>Kill:</b> Kill Object like application if set. Default is (false).</li>
     *   <li><b>Key:</b> Secret key to use to authenticate communication.</li>
     * </ul>
     * </p>
     * @param b Bundle to read the Header from (Code, Kill, key)
     * @return void
     */
    void readPacketHeaderBundle(Bundle b);

    /**
     * Invokes {@code android.os.UserHandle.getUserId} to resolve the given Package UID.
     * <p>
     * Most times, this will just resolve to 0. The utility of this method may not be apparent, as it frequently yields a default value.
     * However, to maintain safety and ensure compatibility for cases where it might be necessary, it is used before database transactions.
     * </p>
     * @return void
     */
    void resolveUserID();

    /**
     * Ensures that the fields 'user' and 'category' are filled/set.
     * <p>
     * If 'user' or 'category' are not set, they will be set to global indicators:
     * <ul>
     *   <li>user: defaults to '0' if not specified. This is defined in the 'UserIdentityPacket' class as field 'GLOBAL_USER'.</li>
     *   <li>category: defaults to 'Global' if not specified. This is defined in the 'UserIdentityPacket' class as field 'GLOBAL_NAMESPACE'.</li>
     * </ul>
     * </p>
     * @return void
     */
    void ensureIdentification();

    /**
     * Generates a Selection Argument Query to be used in the 'query' function for Android's {@code android.content.ContentProvider}.
     * <p>
     * This method uses flags to specify how data is being sent to a query, particularly the order of data.
     * Generic flags are defined in the class 'UserIdentityPacket' and determine how data is read/written within the selection argument.
     * Typically, the following data is being read/written:
     * <ul>
     *   <li><b>user:</b> Package and/or User ID.</li>
     *   <li><b>category:</b> Typically the package name used to specify the location of target data.</li>
     *   <li><b>code:</b> Command used to help determine the specific sub-action to be taken.</li>
     * </ul>
     * Examples of flag usage are:
     * <ul>
     *   <li><b>USER_QUERY_PACKET_ONE:</b> Read/Write data order of (user, category, code).</li>
     *   <li><b>USER_QUERY_PACKET_TWO:</b> Read/Write data order of (category, user, code).</li>
     * </ul>
     * </p>
     * @param flags Help specify how the data is being sent to a query (order specifically).
     * @return {@code eu.faircode.xlua.api.standard.database.SqlQuerySnake}
     */
    SqlQuerySnake generateSelectionArgsQuery(int flags);

    /**
     * Helps read given selection argument data in a specific order or items.
     * The data is obtained from the 'query' function of {@code android.content.ContentProvider}
     * <p>
     * This method utilizes flags to specify the order in which data is read from a query. Generic flags are defined in the class 'UserIdentityPacket' and are used to determine the order data is read from the selection argument. Typically, the data being read includes:
     * <ul>
     *   <li><b>user:</b> Package and/or User ID.</li>
     *   <li><b>category:</b> Typically the package name used to specify the location of target data.</li>
     *   <li><b>code:</b> Command used to determine the specific sub-action to be taken.</li>
     * </ul>
     * For example:
     * <ul>
     *   <li><b>USER_QUERY_PACKET_ONE:</b> Reads data in the order of (user, category, code).</li>
     *   <li><b>USER_QUERY_PACKET_TWO:</b> Reads data in the order of (category, user, code).</li>
     * </ul>
     * </p>
     * @param selection Argument holding the data to be read, typically retrieved from the 'query' function in {@code android.content.ContentProvider}.
     * @param flags Help specify how the data is read from a query and in what order.
     * @return void
     */
    void readSelectionArgsFromQuery(String[] selection, int flags);

    /**
     * Determine if the Identification Fields are Valid
     * <p>
     * Identification Fields can be
     * <ul>
     *   <li><b>user:</b> Package and/or User ID.</li>
     *   <li><b>category:</b> Typically the package name used to specify the location of target data.</li>
     * </ul>
     * @return True if User ID is not NULL and is 0 or Greater AND if Category is a valid String
     */
    boolean isValidIdentity();

    /**
     * Copy Applications Identity into the Identity Object
     * <p>
     * Identification Fields that will be copied by default
     * <ul>
     *   <li><b>user:</b> Package and/or User ID.</li>
     *   <li><b>category:</b> Typically the package name used to specify the location of target data.</li>
     * </ul>
     * @param application Data Holder for the Application Identity
     * @return void
     */
    void identificationFromApplication(AppGeneric application);

    /**
     * Ensures that a code is set (not NULL or EMPTY)
     * <p>
     * @param defaultCode Code to use If Code is NULL or EMPTY
     * @return void
     */
    void ensureCode(int defaultCode);
}
