package nl.remcoder.comfy

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import nl.remcoder.comfy.Models.Room

class EditRoomActivity : AppCompatActivity() {

    val MENU_CREATE_ROOM  = Menu.FIRST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_room)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menu
            .add(0, MENU_CREATE_ROOM, Menu.NONE, R.string.action_create_room)
            .setIcon(R.drawable.ic_check_white_24dp)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        print("selected: " + id)
        //noinspection SimplifiableIfStatement
        if (id == MENU_CREATE_ROOM) {
            val roomName = (findViewById(R.id.roomName) as TextView).text.toString()
            val ipAddress = (findViewById(R.id.ipAddress) as TextView).text.toString()
            val intent = Intent()
            intent.putExtra("room-name", roomName)
            intent.putExtra("ip-address", ipAddress)
            setResult(RESULT_OK, intent)
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
