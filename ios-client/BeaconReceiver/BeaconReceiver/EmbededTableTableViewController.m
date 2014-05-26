//
//  EmbededTableTableViewController.m
//  BeaconReceiver
//
//  Created by Yuwei Xia on 4/9/14.
//  Copyright (c) 2014 Citigroup. All rights reserved.
//

#import "EmbededTableTableViewController.h"

@interface EmbededTableTableViewController ()

@property (nonatomic, weak) NSMutableData *responseData;

@property (nonatomic, strong) NSMutableArray *timeSlots;

@property (assign, nonatomic) BOOL datePickerIsShowing;
@property (assign, nonatomic) BOOL timePickerIsShowing;

@property (strong, nonatomic) NSDateFormatter *dateFormatter;

@property (assign, nonatomic) NSInteger pickerCellRowHeight;

@end

@implementation EmbededTableTableViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.meetingNameTF.text = @"Kevin's meeting";
    self.usersTF.text = @"Kevin";
//    self.meetingNameTF.textColor = [self.tableView tintColor];
//    self.usersTF.textColor = [self.tableView tintColor];
//    UIImageView *tempImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"vetch.png"]];
//    [tempImageView setFrame:self.tableView.frame];
//    self.tableView.backgroundView = tempImageView;
    self.tableView.separatorColor = [UIColor clearColor];
    
    self.tableView.backgroundColor = [UIColor clearColor];
    //self.parentViewController.view.backgroundColor = [UIColor clearColor];
    for (UIView *view in self.tableView.subviews) {
        view.backgroundColor = [UIColor clearColor];
    }
    
    NSDate *today = [NSDate date];
    self.dateFormatter = [[NSDateFormatter alloc] init];
    [self.dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *dateString = [self.dateFormatter stringFromDate:today];
    self.dateLabel.text = [NSString stringWithFormat:@"%@", dateString];
//    self.dateLabel.textColor = [self.tableView tintColor];
    
    [self getAvailableTimeSlotsForTheDate:dateString];
    
    self.datePicker.alpha = 0.0f;

    self.timeSlots = [[NSMutableArray alloc] initWithObjects:@"9-10", @"10-11", @"11-12", @"12-13", @"13-14", @"14-15", @"15-16", @"16-17", nil];
    
    self.timePicker.dataSource = self;
    self.timePicker.delegate = self;
    self.timePicker.alpha = 0.0f;
    self.timeLabel.text = @"9-10";
//    self.timeLabel.textColor = [self.tableView tintColor];
    [[UINavigationBar appearance] setTitleTextAttributes:@{NSForegroundColorAttributeName: [self.tableView tintColor]}];

    self.pickerCellRowHeight = self.datePickerCell.frame.size.height;
}


- (NSArray *)getAvailableTimeSlotsForTheDate: (NSString *)dateString {

    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSString *url = [NSString stringWithFormat:@"http://yuweixia.local:8888/rooms/%@/booking/%@", self.room.name, dateString];
        NSURL *jsonURL = [NSURL URLWithString:url];
        NSData *jsonData = [NSData dataWithContentsOfURL:jsonURL];
        
        NSArray *jsonDict = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
        
        for (NSDictionary *bookingDict in jsonDict) {
            NSString *startTime = [bookingDict objectForKey:@"StartT"];
            NSString *endTime = [bookingDict objectForKey:@"EndT"];
            NSString *timeSlot = [NSString stringWithFormat:@"%@-%@", startTime, endTime];
            //NSLog(@"%@", timeSlot);
            [self.timeSlots removeObject:timeSlot];
        }
    });
    return self.timeSlots;
}

#define kDatePickerIndex 1
#define kTimePickerIndex 3

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat height = self.tableView.rowHeight;
    if (indexPath.section == 1) {
        if (indexPath.row == kDatePickerIndex){
            height = self.datePickerIsShowing ? self.pickerCellRowHeight : 0.0f;
        } else if (indexPath.row == kTimePickerIndex){
            height = self.timePickerIsShowing ? self.pickerCellRowHeight : 0.0f;
        }
    }
    return height;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    //NSLog(@"%d, %d", indexPath.section, indexPath.row);
    if (indexPath.section == 1) {
        //NSLog(@"in section 1");
        if (indexPath.row == 0) {
            //NSLog(@"in row 0");
            if (self.datePickerIsShowing) {
                [self hideDatePickerCell];
            }else {
                [self.tableView endEditing:YES];
                if (self.timePickerIsShowing) {
                    [self hideTimePickerCell];
                }
                [self showDatePickerCell];
            }
        } else if (indexPath.row == 2) {
            //NSLog(@"in row 2");
            if (self.timePickerIsShowing) {
                [self hideTimePickerCell];
            } else {
                [self.tableView endEditing:YES];
                if (self.datePickerIsShowing) {
                    [self hideDatePickerCell];
                }
                [self showTimePickerCell];
            }
        }
    }
    
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
}

-(void)showDatePickerCell {
    self.datePickerIsShowing = YES;
    [self.tableView beginUpdates];
    
    [self.tableView endUpdates];
    
    self.datePicker.hidden = NO;
    self.datePicker.alpha = 0.0f;
    
    [UIView animateWithDuration:0.4 animations:^{
        self.datePicker.alpha = 1.0f;
    }];
}

-(void)hideDatePickerCell {
    self.datePickerIsShowing = NO;
    [self.tableView beginUpdates];
    
    [self.tableView endUpdates];
    
    [UIView animateWithDuration:0.4
                     animations:^{
                         self.datePicker.alpha = 0.0f;
                     }
                     completion:^(BOOL finished) {
                         self.datePicker.hidden = YES;
                     }];
}

-(void)showTimePickerCell {
    self.timePickerIsShowing = YES;
    [self.tableView beginUpdates];
    
    [self.tableView endUpdates];
    
    self.timePicker.hidden = NO;
    self.timePicker.alpha = 0.0f;
    
    [UIView animateWithDuration:0.4 animations:^{
        self.timePicker.alpha = 1.0f;
    }];
}

-(void)hideTimePickerCell {
    self.timePickerIsShowing = NO;
    [self.tableView beginUpdates];
    
    [self.tableView endUpdates];
    
    [UIView animateWithDuration:0.4
                     animations:^{
                         self.timePicker.alpha = 0.0f;
                     }
                     completion:^(BOOL finished) {
                         self.timePicker.hidden = YES;
                     }];
}

-(IBAction)pickerDateChanged:(UIDatePicker *)sender {
    [self updateTextField];
}
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return self.timeSlots.count;
}

-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return [self.timeSlots objectAtIndex:row];
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    //NSLog(@"selected row %d", row);
    self.timeLabel.text = [self.timeSlots objectAtIndex:row];
}

//-(UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view {
//    
//    UILabel *pickerLabel = (UILabel *) view;
//    pickerLabel.textColor = [UIColor whiteColor];
//    return pickerLabel;
//}

-(void)updateTextField{
    //UIDatePicker *datePicker = (UIDatePicker *)self.dateTF.inputView;
    NSDate *selectedDate = self.datePicker.date;
    NSString *newDate = [self.dateFormatter stringFromDate:selectedDate];
    self.dateLabel.text = [NSString stringWithFormat:@"%@", newDate];
    [self getAvailableTimeSlotsForTheDate:newDate];
    [self.timePicker reloadAllComponents];
}

- (void)signUpForKeyboardNotifications {
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow) name:UIKeyboardWillShowNotification object:nil];
}

-(void)keyboardWillShow {
    if (self.datePickerIsShowing) {
        [self hideDatePickerCell];
    }
    if (self.timePickerIsShowing) {
        [self hideTimePickerCell];
    }
}
@end
