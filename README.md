# arch_comp_mvi
MVI architecture pattern implementation library. 
Implementation based on [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/).
The core interface [ViewStateInteractor](https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/ViewStateInteractor.kt)
abstraction got only 2 
entry points for veiw layer: [LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData)
contatiner with viewState, and method that can take view intents.

The basic implementation of core component, [ViewStateInteractorImpl](https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/ViewStateInteractor.kt)
, assumes that connection with "domain logic" will also happen through
[LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData) in thread save manner.

[Data flow scheme]:https://github.com/bugDim88/arch_comp_mvi/blob/master/lib_mvi_tutorials/data_flow_scheme.png




