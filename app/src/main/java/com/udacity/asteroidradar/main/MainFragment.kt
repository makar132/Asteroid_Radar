package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.model.AsteroidsListRecyclerView

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private val adapter: AsteroidsListRecyclerView by lazy {
        AsteroidsListRecyclerView()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        adapter.onClickListener = {
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        }
        binding.activityMainImageOfTheDayLayout.setOnClickListener {
            displayPictureOfTheDayDetails()
        }
        binding.asteroidRecycler.adapter = adapter
        binding.statusLoadingWheel.visibility = View.VISIBLE
        viewModel.getAsteroidsOffline()
        observe()

        setHasOptionsMenu(true)
        return binding.root
    }


    @SuppressLint("LogNotTimber")
    private fun observe() {

        viewModel.getAsteroidsStatus.observe(viewLifecycleOwner) {
            if (it == "success") {
                if (binding.statusLoadingWheel.visibility == View.VISIBLE) {
                    binding.statusLoadingWheel.visibility = View.GONE
                }

                viewModel.clearGetAsteroids()
            } else if (it == "error") {
                if (binding.statusLoadingWheel.visibility == View.GONE) {
                    binding.statusLoadingWheel.visibility = View.VISIBLE
                }

                val conManager =
                    context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val internetInfo = conManager.activeNetworkInfo
                if ((internetInfo != null) && internetInfo.isConnected) {
                    viewModel.getAsteroidsOnline()
                } else {
                    viewModel.getAsteroidsOffline()
                }


            }
        }
        viewModel.asteroids.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
            adapter.setList(it)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.view_today_asteroids_menu -> {
                binding.statusLoadingWheel.visibility = View.VISIBLE
                viewModel.getTodayAsteroidsOffline()
                binding.invalidateAll()
                binding.statusLoadingWheel.visibility = View.GONE
            }
            R.id.view_week_asteroids_menu -> {
                binding.statusLoadingWheel.visibility = View.VISIBLE
                viewModel.getAsteroidsOffline()
                binding.invalidateAll()
                binding.statusLoadingWheel.visibility = View.GONE
            }
        }
        return true
    }

    @SuppressLint("UseCompatLoadingForDrawables", "LogNotTimber")
    override fun onResume() {
        super.onResume()
        if (binding.activityMainImageOfTheDay.drawable.constantState == resources.getDrawable(R.drawable.placeholder_picture_of_day).constantState) {
            binding.activityMainImageOfTheDayTitle.text =
                "connect to internet to see the picture of the day"
            val conManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val internetInfo = conManager.activeNetworkInfo
            if ((internetInfo != null) && internetInfo.isConnected) {
                viewModel.getPictureOfTheDay(
                    binding.activityMainImageOfTheDay,
                    binding.activityMainImageOfTheDayTitle
                )
            } else {
                viewModel.getPictureOfTheDayOffline(
                    binding.activityMainImageOfTheDay,
                    binding.activityMainImageOfTheDayTitle
                )
            }
        }
        viewModel.potdErrorStatus.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(
                    context,
                    "error downloading picture of th day : $it",
                    Toast.LENGTH_LONG
                ).show()
                viewModel.clearptodError()
            }
        }
    }

    private fun displayPictureOfTheDayDetails() {
        val title = viewModel.getPotdTitle()
        val content = viewModel.getPotdExplanation()
        if (title.isNotEmpty() && content.isNotEmpty()) {

            val builder = AlertDialog.Builder(requireActivity())
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(android.R.string.ok, null)

            builder.create().show()
        }
    }
}
