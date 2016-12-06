package nl.remcoder.comfy

import android.content.Context
import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import kotlinx.android.synthetic.main.activity_edit_room.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import nl.remcoder.comfy.Models.Room
import org.jetbrains.anko.startActivityForResult
import java.io.File
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.reflect.Type


val rooms = mutableListOf<Room>()

class MainActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: ViewPager? = null
    private val filename = "rooms"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        println("reading file: " + filename)
        var inputStream : FileInputStream
        var json = ""
        try {
            inputStream = openFileInput(filename)
            val bytes = inputStream.readBytes()
            json = String(bytes)
            inputStream.close()
            println(json)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val gson = Gson()
        val listType = object : TypeToken<MutableList<Room>>() {}.type
        val obj = gson.fromJson<MutableList<Room>>(json, listType)

        rooms.clear()

        for(r in obj) {
            rooms.add(r)
        }


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter


        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            println("click")

            startActivityForResult<EditRoomActivity>(0)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        println("requestcode " + requestCode)
        println("resultCode" + resultCode)
        // Check which request we're responding to
        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                val roomName = data.getStringExtra("room-name")
                val ipAddress = data.getStringExtra("ip-address")

                var newRoom = Room(roomName, 0.0, 0.0, ipAddress)

                Snackbar.make(mViewPager!!, "'${newRoom.name}' added", Snackbar.LENGTH_LONG)
                        .show()

                rooms.add(newRoom)

                // store rooms
                val gson = Gson()
                val json = gson.toJson( rooms )
                val outputStream: FileOutputStream

                println("saving file: " + filename)

                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE)
                    outputStream.write(json.toByteArray())
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                mSectionsPagerAdapter?.notifyDataSetChanged()

                mViewPager!!.setCurrentItem(rooms.count()-1, true)

            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        val id = item.itemId
//        print("selected: " + id)
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class RoomFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val roomFragmentView = inflater!!.inflate(R.layout.fragment_main, container, false)

            val roomIndex = arguments.getInt(ARG_ROOM_INDEX)
            val currentRoom = rooms[roomIndex]

            roomFragmentView.RoomName.text = currentRoom.name
            roomFragmentView.RoomTemperature.text = currentRoom.temperature.toString() + "°"
            roomFragmentView.RoomHumidity.text = currentRoom.humidity.toString() + "%"
            roomFragmentView.RoomIpAddress.text = currentRoom.ipAddress

            return roomFragmentView
        }

        companion object {

            private val ARG_ROOM_INDEX = "room_index"

            fun newInstance(sectionNumber: Int): RoomFragment {
                val fragment = RoomFragment()
                val args = Bundle()
                args.putInt(ARG_ROOM_INDEX, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return RoomFragment.newInstance(position)
        }

        override fun getCount(): Int {
            return rooms.count()
        }

        override fun getPageTitle(position: Int) = rooms[position].name
    }
}
