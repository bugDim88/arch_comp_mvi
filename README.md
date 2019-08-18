# arch_comp_mvi
MVI architecture pattern implementation library. 
Implementation based on [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/).
The core interface [ViewStateInteractor](https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/ViewStateInteractor.kt)
abstraction got only 2 
entry points for veiw layer: [LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData)
contatiner with viewState, and method that can take view intents.

The basic implementation of core component, [ViewStateInteractorImpl](https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/ViewStateInteractor.kt)
, assumes that connection with "domain logic" will also happen through
[LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData) in thread save manner. It's recomended to use [UseCase](https://github.com/google/iosched/blob/master/shared/src/main/java/com/google/samples/apps/iosched/shared/domain/UseCase.kt) from [Google ioshed](https://github.com/google/iosched) repo to implement domain logic. 

![Data flow scheme](https://github.com/bugDim88/arch_comp_mvi/blob/master/lib_mvi_tutorials/data_flow_scheme.png "Data flow scheme")

## How to use.
The core components of MVI pattern flow is *ViewState* and *ViewIntent* objects. The *ViewState* it's simple POJO
class with all propoerties that view layer must to know to correct present current state on the screen. In Kotlin using *data class* is appropriate for this purpose. Example of *ViewState* imlementation:

```
data class ViewState(
 isLoading: HandledData<Boolean>,
 counter: HandledData<Int>,
 ...
)
```

[HandledData](https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/HandledData.kt) help to prevent handling property multiply times in every *ViewState* update.

*ViewIntent* must present all interactions that [ViewStateInteractor](https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/ViewStateInteractor.kt) can handle. 

To implement abstract class [ViewStateInteractorImpl](https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/ViewStateInteractor.kt)

