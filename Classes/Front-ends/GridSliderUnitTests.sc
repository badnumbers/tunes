GridSliderUnitTests : BNUnitTest {
	test_valueForY_bottomPixelIsZero {
		this.assertEquals(GridSlider.valueForY(47, 48, 0), 0);
	}

	test_valueForY_clipsOutsideTheTrack {
		this.assertEquals(GridSlider.valueForY(-4, 48, 0), 1);
		this.assertEquals(GridSlider.valueForY(100, 48, 0), 0);
	}

	test_valueForY_insetTrack {
		this.assertEquals(GridSlider.valueForY(2, 48, 2), 1);
		this.assertEquals(GridSlider.valueForY(45, 48, 2), 0);
		this.assertEquals(GridSlider.valueForY(0, 48, 2), 1);
		this.assertEquals(GridSlider.valueForY(47, 48, 2), 0);
	}

	test_valueForY_midpoint {
		this.assertEquals(GridSlider.valueForY(23.5, 48, 0), 0.5);
	}

	test_valueForY_topIsOne {
		this.assertEquals(GridSlider.valueForY(0, 48, 0), 1);
	}

	test_value__clipsToUnitInterval {
		var slider;
		slider = GridSlider(Color.white, Color.black);
		slider.value_(1.5);
		this.assertEquals(slider.value, 1);
		slider.value_(-0.2);
		this.assertEquals(slider.value, 0);
		slider.value_(0.25);
		this.assertEquals(slider.value, 0.25);
	}
}
