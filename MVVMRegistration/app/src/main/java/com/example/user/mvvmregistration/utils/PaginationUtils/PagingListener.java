package com.example.user.mvvmregistration.utils.PaginationUtils;

import rx.Observable;

public interface PagingListener<T> {
    Observable<T> onNextPage(int offset);
}