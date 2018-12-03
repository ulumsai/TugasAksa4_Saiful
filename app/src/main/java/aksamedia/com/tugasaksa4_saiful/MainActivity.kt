package aksamedia.com.tugasaksa4_saiful

import aksamedia.com.tugasaksa4_saiful.adapter.TeamAdapter
import aksamedia.com.tugasaksa4_saiful.api.ApiRepository
import aksamedia.com.tugasaksa4_saiful.contract.MainView
import aksamedia.com.tugasaksa4_saiful.model.League
import aksamedia.com.tugasaksa4_saiful.model.Team
import aksamedia.com.tugasaksa4_saiful.presenter.MainPresenter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.support.v4.onRefresh

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var presenter: MainPresenter
    private lateinit var adapter: TeamAdapter
    private var data_tim: MutableList<Team> = mutableListOf()
    private var data_liga:MutableList<League> = mutableListOf()
    private lateinit var namaLiga :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setupSpiner()
        setupRecyclerview()
        setupPresenter()

        listLiga.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                namaLiga = listLiga.selectedItem.toString()
                namaLiga = namaLiga.replace(" ","_",true)
                Log.d("NamaLiga ",""+namaLiga)
                presenter.getTeamList(namaLiga)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        swipeRefresh.onRefresh {
            presenter.getTeamList(namaLiga)
        }
    }

//    fun setupSpiner() {
//        val spinerItem = resources.getStringArray(R.array.league)
//        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinerItem)
//        listLiga.adapter = spinnerAdapter
//    }

    fun setupPresenter(){
        val request = ApiRepository()
        val gson = Gson()
        presenter = MainPresenter(this, request, gson)
    }

    fun setupRecyclerview(){
        adapter = TeamAdapter(this, data_tim)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun showLoading() {
        loadingBar.visibility= View.VISIBLE
    }

    override fun hideLoading() {
        loadingBar.visibility= View.INVISIBLE
    }

    override fun showTeamList(data: List<Team>) {
        swipeRefresh.isRefreshing = false
        data_tim.clear()
        data_tim.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun showListLiga(data: List<League>) {
        data_liga.clear()
        data_liga.addAll(data)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, data_liga)
        listLiga.adapter = spinnerAdapter
    }
}
