package git.ice.drinker.lib.directory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import git.ice.drinker.lib.directory.model.DirectoryBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        panel_directory.folderCheckable = false
        panel_directory.fileCheckable = true
        panel_directory.setOnItemClickListener { iDirectoryBean, b ->
            val text = panel_directory.checkedDirectories.joinToString { it.name }
            tv_text.text = "checked: $text"
        }
        panel_directory.initData(getData())
    }

    private fun getData(): DirectoryBean {
        val subBean3 = DirectoryBean()
        subBean3.id = 4
        subBean3.name = "child node 3"

        val subBean2 = DirectoryBean()
        subBean2.id = 3
        subBean2.name = "child node 2"
        subBean2.isFolder = false

        val subBean1 = DirectoryBean()
        subBean1.id = 2
        subBean1.name = "child node 1"
        subBean1.isFolder = false

        val parentBean = DirectoryBean()
        parentBean.id = 1
        parentBean.name = "parent node"
        parentBean.isFolder = true
        val subBeans1 = arrayListOf<DirectoryBean>(subBean2, subBean3)
        parentBean.setDirectories(subBeans1)

        val subBeans2 = arrayListOf<DirectoryBean>(parentBean, subBean1)
        val rootBean = DirectoryBean()
        rootBean.id = 0
        rootBean.isFolder = true
        rootBean.name = "root"
        rootBean.setDirectories(subBeans2)
        Log.v("MainActivity", "rootBean: $rootBean")
        return rootBean
    }
}
