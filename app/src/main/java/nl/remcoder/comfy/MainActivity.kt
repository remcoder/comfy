package nl.remcoder.comfy

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
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import nl.remcoder.comfy.Models.Room


val rooms = mutableListOf(
        Room("Woonkamer", 20.0, 42.0, "0.0.0.0"),
        Room("Kinderkamer", 21.5, 38.0, "0.0.0.0"),
        Room("Badkamer", 22.5, 65.8, "0.0.0.0")
)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter


        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            println("click")
            Snackbar.make(view, "'Keuken' added", Snackbar.LENGTH_LONG).setAction("Action", null).show()

            rooms.add(Room("Keuken", 20.0, 40.0, "0.0.0.0"))

            mSectionsPagerAdapter?.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        print("selected: " + id)
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

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