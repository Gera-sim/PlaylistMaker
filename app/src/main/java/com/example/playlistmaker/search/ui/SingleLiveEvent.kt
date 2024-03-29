package com.example.playlistmaker.search.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

// Спринт 16, Шаг 6.2:

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            } } }

    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }}


/***
Класс SingleLiveEvent представляет собой вариант MutableLiveData,
который уведомляет своих наблюдателей только один раз об изменении
значения. Это полезно в ситуациях, когда вы хотите отправить событие,
которое должно быть обработано только один раз, например,
показ снэкбара или навигацию к другому фрагменту.

Обычное поведение MutableLiveData - уведомлять всех своих
наблюдателей каждый раз, когда значение меняется.
Однако, в некоторых случаях, это может привести к повторению
обработки одного и того же события. Например, если пользователь
поворачивает устройство, это может привести к повторному вызову
метода onChanged, который в свою очередь может вызвать повторное
действие (например, показ снэкбара или переход к другому экрану).
SingleLiveEvent предназначен для решения этой проблемы.

Как работает SingleLiveEvent:
У класса есть член pending, который является AtomicBoolean.
Это означает, что значение может быть безопасно изменено из разных
потоков. Это полезно, если значение MutableLiveData устанавливается
из разных потоков.
В методе observe вызывается метод observe родительского класса
(MutableLiveData). Вместо непосредственной передачи наблюдателя
в родительский класс, создается новый наблюдатель, который проверяет
значение pending. Если pending равно true, это означает, что было
новое событие, которое еще не было обработано. В этом случае оно
уведомляет исходного наблюдателя и устанавливает pending в false,
чтобы событие не было обработано повторно.
В методе setValue сначала устанавливается pending в true, затем
вызывается setValue родительского класса. Это гарантирует, что
следующий наблюдатель, который будет уведомлен о новом значении,
увидит, что pending равно true.

В итоге, SingleLiveEvent уведомляет каждого наблюдателя только
один раз о каждом новом значении. Это делает его идеальным для
использования в ситуациях, когда необходимо отправить событие,
которое должно быть обработано только один раз.*/