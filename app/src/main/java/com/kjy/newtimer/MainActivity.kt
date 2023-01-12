package com.kjy.newtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.kjy.newtimer.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // 전체 시간을 저장하는 값, 기본값 0초
    var total = 0

    // 시작됨을 체크할 수 있는 started 선언, 시작되지 않았으므로 기본값 false
    var started = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // total과 started를 이용해서 화면의 시간값을 출력하는 Handler 구현, handler 변수에 저장
        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                // 60으로 나눈 몫은 분 단위로
                val minute = String.format("%02d", total / 60)
                val second = String.format("%02d", total % 60)
                binding.textTimer.text = "$minute:$second"
                // 60으로 나눈 나머지는 초 단위로 사용해서 적용
            }
        }

        /**
         * 버튼이 클릭되면 먼저 started를 true로 변경하고 새로운 스레드 실행
         * 스레드는 while문의 started가 true인 동안 while 문을 반복하면서 1초에 한 번씩 total의 값을 1씩 증가시키고
         * 핸들러에 메시지를 전송.
         *
         */

        binding.buttonStart.setOnClickListener {
            started = true
            thread(start = true) {
                while (started) {
                    Thread.sleep(1000)          // 1초에 한번씩
                    if (started) {
                        total += 1
                        handler?.sendEmptyMessage(0)
                    }
                }
            }
        }

        // 종료 코드 구현
        binding.buttonStop.setOnClickListener {
            if(started) {
                started = false
                total = 0
                binding.textTimer.text = "00:00"
            }
        }

    }
}