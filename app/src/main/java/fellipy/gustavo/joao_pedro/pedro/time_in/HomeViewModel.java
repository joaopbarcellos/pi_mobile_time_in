package fellipy.gustavo.joao_pedro.pedro.time_in;

import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    int navigationOpSelected = R.id.homeOp;
    public void setNavigationOpSelected(int navigationOpSelected){
        this.navigationOpSelected = navigationOpSelected;
    }
}
