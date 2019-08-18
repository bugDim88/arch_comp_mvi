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

[HandledData](https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/HandledData.kt) here to help prevent handling property multiply times in every *ViewState* update.

*ViewIntent* must present all interactions that [ViewStateInteractor](https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/ViewStateInteractor.kt) can handle. Kotlin *sealed class* is suitable for this task. *ViewIntent* example:

```
sealed class ViewIntent{
  data class InitIntent(repoId: Int): ViewIntent
  object ConfigChangeIntent(): ViewIntent
}
```
Here you can use *data class* for ViewIntent's with arguments and *objects* for ViewIntent's without arguments.

[ViewStateInteractorImpl](https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/ViewStateInteractor.kt) built on three (Reducer)[https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/ViewStateInteractor.kt] functions. This functions get *ViewState* in input and gives updated *ViewState* in output, optionaly you can give som additional argument for this function. ViewState update by these functions is shown in the scheme.

![Reducers scheme](https://github.com/bugDim88/arch_comp_mvi/blob/master/lib_mvi_tutorials/reducers_scheme.png "Reducers scheme")

* The *intentReducer* is required reducer that covert *ViewIntent* to new *ViewState*. 
* The *useCaseReducer* is optional reducer that converts domain UseCase result to new *ViewState*. Attached to [ViewStateInteractorImpl](https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/ViewStateInteractor.kt) through attachUseCaseReducer(...) method.
* The *resultStateReducer* is also optional and intended for apply some udaptes to *ViewState* before final object will be passed to *View*. It is userful for situation when you need to compare some old and current state stuff to updated some fields in *ViewState*.

Implemented [ViewStateInteractor](https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/ViewStateInteractor.kt) you can pass to the activity or fragment with [ViewModel](https://developer.android.com/reference/android/arch/lifecycle/ViewModel) throug inheritence with delegation or composition.
That way [ViewModel](https://developer.android.com/reference/android/arch/lifecycle/ViewModel) will provide all state safety stuff on configuration change.
Inheritance example:
```
ViewStateInteractorVM<S, I>(reducerDelegate: ViewStateInteractor<S, I>) : ViewModel(),
    ViewStateInteractor<S, I> by reducerDelegate
```
On fragment side you simply observer *ViewState* [LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData) and pass user interactions through *onViewIntent(...)* methods back to interactor.

## Testing
Unit testing of [ViewStateInteractor](https://github.com/bugDim88/arch_comp_mvi/blob/master/arch_comp_mvi_lib/src/main/java/com/bugdim88/arch_comp_mvi_lib/ViewStateInteractor.kt) is simple as [LiveData](https://developer.android.com/reference/android/arch/lifecycle/LiveData) holder testing. You just pass monkey *ViewIntents* and assert updated *ViewState*.
You can find example in [demo project](https://github.com/bugDim88/arch_comp_mvi/tree/master/app) inside a repository.




