package com.zhi.contacts;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
public class ContactTest extends AndroidTestCase {

    private static final String TAG = "ContactTest";
    /*获取联系人信息：姓名、电话*/
    public void testContacts() {
        String name = "", phone = "";
        List<Person> persons = new ArrayList<Person>();
        ContentResolver resolver = this.getContext().getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        Cursor cursorContact = resolver.query(uri, new String[]{"_id"}, null, null, null);
        persons.clear();
        while (cursorContact.moveToNext()) {
            int id = cursorContact.getInt(0);
            uri = Uri.parse("content://com.android.contacts/contacts/"+ id+"/data");
            Cursor cursorData = resolver.query(uri,
                    new String[]{"mimetype", "data1"}, null, null, null);
            while (cursorData.moveToNext()) {
                String mimetype = cursorData.getString(cursorData.getColumnIndex("mimetype"));
                if (mimetype.equals("vnd.android.cursor.item/name")) {
                    name = cursorData.getString(cursorData.getColumnIndex("data1"));
                }
                if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                    phone = cursorData.getString(cursorData.getColumnIndex("data1"));
                }
            }
            persons.add(new Person(name, phone));
            cursorContact.close();
        }

        for (Person person : persons) {
            Log.e(TAG, person.toString());
        }
    }
    /*根据手机号查询用户姓名*/
    public void testQueryByNum(){
        String number = "18218364949";
        ContentResolver resolver = this.getContext().getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + number);
        Cursor cursor = resolver.query(uri, new String[]{"display_name"}, null, null, null);
        if (cursor.moveToFirst()){
            String name = cursor.getString(0);
            Log.e(TAG, name);
        }
        cursor.close();
    }

    /*添加联系人：事务*/
    public void testInsertContact() throws Exception{
        ContentResolver resolver = this.getContext().getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation operation = ContentProviderOperation.newInsert(uri)
                .withValue("account_name", null)
                .build();
        operations.add(operation);

        uri = Uri.parse("content://com.android.contacts/data");
        ContentProviderOperation op1 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("row_contact_id", 0)
                .withValue("data2", "龚翔宇")
                .withValue("mimetype", "vnd.android.cursor.item/name")
                .build();
        operations.add(op1);

        ContentProviderOperation op2 = ContentProviderOperation.newInsert(uri)
                .withValueBackReference("row_contact_id", 0)
                .withValue("data1", "18218364949")
                .withValue("data2", "2")
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .build();
        operations.add(op2);
        if(null == resolver || operations == null){
            Log.e(TAG, "---------------error---------======");
        }
        resolver.applyBatch("com.android.contacts", operations);  // 这一句总是报空指针，参数都非空，未添加上去
    }
}
