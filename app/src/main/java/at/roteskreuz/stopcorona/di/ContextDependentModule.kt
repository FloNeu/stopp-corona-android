package at.roteskreuz.stopcorona.di

import android.app.Activity
import android.security.KeyPairGeneratorSpec
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.WorkManager
import at.roteskreuz.stopcorona.model.assets.AssetInteractor
import at.roteskreuz.stopcorona.model.assets.AssetInteractorImpl
import at.roteskreuz.stopcorona.model.receivers.BatterySaverStateReceiver
import at.roteskreuz.stopcorona.model.receivers.BluetoothStateReceiver
import at.roteskreuz.stopcorona.model.repositories.other.ContextInteractor
import at.roteskreuz.stopcorona.model.repositories.other.ContextInteractorImpl
import at.roteskreuz.stopcorona.model.repositories.other.OfflineSyncer
import at.roteskreuz.stopcorona.model.repositories.other.OfflineSyncerImpl
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.nearby.Nearby
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

/**
 * Module for providing android context related dependencies.
 */
internal val contextDependentModule = module {

    single<ContextInteractor> {
        ContextInteractorImpl(
            context = get()
        )
    }

    single<AssetInteractor> {
        AssetInteractorImpl(
            appDispatchers = get(),
            moshi = get(),
            filesRepository = get()
        )
    }

    single { (activity: Activity) ->
        Nearby.getMessagesClient(activity)
    }

    single {
        GoogleApiClient.Builder(androidContext())
    }

    @Suppress("DEPRECATION")
    single {
        KeyPairGeneratorSpec.Builder(androidContext())
    }

    single<OfflineSyncer>(createOnStart = true) {
        OfflineSyncerImpl(
            appDispatchers = get(),
            contextInteractor = get(),
            sharedPreferences = get(),
            processLifecycleOwner = ProcessLifecycleOwner.get(),
            configurationRepository = get(),
            dataPrivacyRepository = get(),
            infectionMessengerRepository = get(),
            pushMessagingRepository = get()
        )
    }

    single {
        WorkManager.getInstance(androidContext())
    }

    single {
        BluetoothStateReceiver()
    }

    single {
        BatterySaverStateReceiver()
    }

    single {
        LocalBroadcastManager.getInstance(androidContext())
    }
}
