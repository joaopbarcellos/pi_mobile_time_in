package fellipy.gustavo.joao_pedro.pedro.time_in.Model;

import androidx.lifecycle.ViewModel;

import fellipy.gustavo.joao_pedro.pedro.time_in.R;

public class HomeViewModel extends ViewModel {

    int navigationOpSelected = R.id.homeOp;
    public void setNavigationOpSelected(int navigationOpSelected){
        this.navigationOpSelected = navigationOpSelected;
    }
}
