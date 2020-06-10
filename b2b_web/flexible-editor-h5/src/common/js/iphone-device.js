/*
 * @Author: yaowei
 * @Date: 2019-08-04 14:12:34
 * @LastEditTime: 2019-11-29 10:36:47
 * @Description: 根据判断物理分辨率以及倍数，结合 renderer.js 返回的 Apple * GPU，获取手机具体机型（主要针对 iPhone 以及 iPad）
 */

/**
 * @param  {string} renderer
 */
function getModel(renderer) {
  // 判断屏幕物理分辨率/倍数
  if (window.screen.height / window.screen.width === 1024 / 768) {
    // iPad, iPad 2, iPad Mini
    if (window.devicePixelRatio === 1) {
      switch (renderer) {
        case 'PowerVR SGX 535':
          return 'iPad'
        case 'PowerVR SGX 543':
          return 'iPad 2, Mini'
        default:
          return 'iPad, iPad 2, iPad Mini'
      }

      // iPad 3, 4, 5, Mini 2, Mini 3, Mini 4, Air, Air 2
    } else {
      switch (renderer) {
        case 'PowerVR SGX 543':
          return 'iPad 3'
        case 'PowerVR SGX 554':
          return 'iPad 4'
        case 'Apple A7 GPU':
          return 'iPad Air, Mini 2, Mini 3'
        case 'Apple A8X GPU':
          return 'iPad Air 2'
        case 'Apple A8 GPU':
          return 'iPad Mini 4'
        case 'Apple A9 GPU':
          return 'iPad 5, Pro 9.7'
        default:
          return 'iPad 3, 4, 5, Mini 2, Mini 3, Mini 4, Air, Air 2'
      }
    }

    // iPad Pro 10.5
  } else if (window.screen.height / window.screen.width === 1112 / 834) {
    return 'iPad Pro 10.5'

    // iPad Pro 11
  } else if (window.screen.height / window.screen.width === 2388 / 1668) {
    return 'iPad Pro 11'

    // iPad Pro 12.9, Pro 12.9 (2nd Gen), Pro 12.9 (3rd Gen)
  } else if (window.screen.height / window.screen.width === 1366 / 1024) {
    switch (renderer) {
      case 'Apple A12X GPU':
        return 'iPad Pro 12.9 (3rd Gen)'
      case 'Apple A10X GPU':
        return 'iPad Pro 12.9 (2nd Gen)'
      case 'Apple A9 GPU':
        return 'iPad Pro 12.9'
      default:
        return 'iPad Pro 12.9, Pro 12.9 (2nd Gen), Pro 12.9 (3rd Gen)'
    }

    // iPhone 12 Pro Max / 13 Pro Max
  } else if ((window.screen.height / window.screen.width === 926 / 428) && (window.devicePixelRatio === 3)) {
    switch (renderer) {
      case 'Apple A14 GPU':
        return 'iPhone 12 Pro Max'
      case 'Apple A15 GPU':
        return 'iPhone 13 Pro Max'
      default:
        return 'iPhone 13 Pro Max, iPhone 12 Pro Max'
    }
    // iPhone 12 / 12 Pro / 13 / 13 Pro
  } else if ((window.screen.height / window.screen.width === 844 / 390) && (window.devicePixelRatio === 3)) {
    switch (renderer) {
      case 'Apple A14 GPU':
        return 'iPhone 12'
      case 'Apple A15 GPU':
        return 'iPhone 13'
      default:
        return 'iPhone 13 Pro, 13, 12, 12 Pro'
    }
    // iPhone 12 mini / 13 mini
  } else if ((window.screen.height / window.screen.width === 780 / 360) && (window.devicePixelRatio === 3)) {
    switch (renderer) {
      case 'Apple A14 GPU':
        return 'iPhone 12 mini'
      case 'Apple A15 GPU':
        return 'iPhone 13 mini'
      default:
        return 'iPhone 13 mini, 12 mini'
    }
    // iPhone XS Max/11 Pro Max
  } else if ((window.screen.height / window.screen.width === 896 / 414) && (window.devicePixelRatio === 3)) {
    switch (renderer) {
      case 'Apple A12 GPU':
        return 'iPhone XS Max'
      case 'Apple A13 GPU':
        return 'iPhone 11 Pro Max'
      default:
        return 'iPhone XS Max, 11 Pro Max'
    }

    // iPhone XR/11
  } else if ((window.screen.height / window.screen.width === 896 / 414) && (window.devicePixelRatio === 2)) {
    switch (renderer) {
      case 'Apple A12 GPU':
        return 'iPhone XR'
      case 'Apple A13 GPU':
        return 'iPhone 11'
      default:
        return 'iPhone 11, iPhone XR'
    }

    // iPhone X/XS/11 Pro
  } else if ((window.screen.height / window.screen.width === 812 / 375) && (window.devicePixelRatio === 3)) {
    switch (renderer) {
      case 'Apple A11 GPU':
        return 'iPhone X'
      case 'Apple A12 GPU':
        return 'iPhone XS'
      case 'Apple A13 GPU':
        return 'iPhone 11 Pro'
      default:
        return 'iPhone 11 Pro, X, XS'
    }

    // iPhone 6+/6s+/7+/8+
  } else if ((window.screen.height / window.screen.width === 736 / 414) && (window.devicePixelRatio === 3)) {
    switch (renderer) {
      case 'Apple A8 GPU':
        return 'iPhone 6 Plus'
      case 'Apple A9 GPU':
        return 'iPhone 6s Plus'
      case 'Apple A10 GPU':
        return 'iPhone 7 Plus'
      case 'Apple A11 GPU':
        return 'iPhone 8 Plus'
      default:
        return 'iPhone 7 Plus, 6s Plus, 6 Plus or 8 Plus'
    }

    // iPhone 6+/6s+/7+/8+ in zoom mode
  } else if ((window.screen.height / window.screen.width === 667 / 375) && (window.devicePixelRatio === 3)) {
    switch (renderer) {
      case 'Apple A8 GPU':
        return 'iPhone 6 Plus (display zoom)'
      case 'Apple A9 GPU':
        return 'iPhone 6s Plus (display zoom)'
      case 'Apple A10 GPU':
        return 'iPhone 7 Plus (display zoom)'
      case 'Apple A11 GPU':
        return 'iPhone 8 Plus (display zoom)'
      default:
        return 'iPhone 6 Plus, 6s Plus, 7 Plus or 8 Plus (display zoom)'
    }

    // iPhone 6/6s/7/8/SE2
  } else if ((window.screen.height / window.screen.width === 667 / 375) && (window.devicePixelRatio === 2)) {
    switch (renderer) {
      case 'Apple A8 GPU':
        return 'iPhone 6'
      case 'Apple A9 GPU':
        return 'iPhone 6s'
      case 'Apple A10 GPU':
        return 'iPhone 7'
      case 'Apple A11 GPU':
        return 'iPhone 8'
      case 'Apple A13 GPU':
        return 'iPhone SE2'
      default:
        return 'iPhone 8, 6s, 7, 6, SE2'
    }

    // iPhone 5/5C/5s/SE or 6/6s/7/8 in zoom mode
  } else if ((window.screen.height / window.screen.width === 568 / 320) && (window.devicePixelRatio === 2)) {
    switch (renderer) {
      case 'PowerVR SGX 543':
        return 'iPhone 5 or 5c'
      case 'Apple A7 GPU':
        return 'iPhone 5s'
      case 'Apple A8 GPU':
        return 'iPhone 6 (display zoom)'
      case 'Apple A9 GPU':
        return 'iPhone SE or 6s (display zoom)'
      case 'Apple A10 GPU':
        return 'iPhone 7 (display zoom)'
      case 'Apple A11 GPU':
        return 'iPhone 8 (display zoom)'
      default:
        return 'iPhone 8, 5C, 5S, SE or 6, 6s, 5 and 8 (display zoom)'
    }

    // iPhone 4/4s
  } else if ((window.screen.height / window.screen.width === 480 / 320) && (window.devicePixelRatio === 2)) {
    switch (renderer) {
      case 'PowerVR SGX 535':
        return 'iPhone 4'
      case 'PowerVR SGX 543':
        return 'iPhone 4s'
      default:
        return 'iPhone 4 or 4s'
    }

    // iPhone 1/3G/3GS
  } else if ((window.screen.height / window.screen.width === 480 / 320) && (window.devicePixelRatio === 1)) {
    switch (renderer) {
      case 'ALP0298C05':
        return 'iPhone 3GS'
      case 'S5L8900':
        return 'iPhone 1, 3G'
      default:
        return 'iPhone 1, 3G or 3GS'
    }
  } else {
    return 'Not Found ~'
  }
}

export {
  getModel
}
