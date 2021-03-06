/**
 * Copyright (c) 2017-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.litho;

import android.graphics.Rect;
import android.support.annotation.VisibleForTesting;

import java.util.Collections;
import java.util.List;

/**
 * Holds information about a {@link TestOutput}.
 */
public class TestItem {
  private String mTestKey;
  private final Rect mBounds = new Rect();
  private ComponentHost mHost;
  private Object mContent;

  @VisibleForTesting
  public String getTestKey() {
    return mTestKey;
  }

  void setTestKey(String testKey) {
    mTestKey = testKey;
  }

  @VisibleForTesting
  public Rect getBounds() {
    return mBounds;
  }

  void setBounds(Rect bounds) {
    mBounds.set(bounds);
  }

  void setBounds(int left, int top, int right, int bottom) {
    mBounds.set(left, top, right, bottom);
  }

  @VisibleForTesting
  public ComponentHost getHost() {
    return mHost;
  }

  @VisibleForTesting
  public String getTextContent() {
    final List<CharSequence> textItems = getTextItems();
    final StringBuilder sb = new StringBuilder();
    for (int i = 0, size = textItems.size(); i < size; i++) {
      sb.append(textItems.get(i));
    }

    return sb.toString();
  }

  public List<CharSequence> getTextItems() {
    return
        ComponentHostUtils
            .extractTextContent(Collections.singletonList(mContent))
            .getTextItems();
  }

  void setHost(ComponentHost host) {
    mHost = host;
  }

  void setContent(Object content) {
    mContent = content;
  }

  Object getContent() {
    return mContent;
  }

  void release() {
    mTestKey = null;
    mBounds.setEmpty();
    mHost = null;
  }
}
